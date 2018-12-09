package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService implements Serializable {
	private MoneyRegister moneyRegister;
	private AtomicInteger currentTick;

	public SellingService(String name,MoneyRegister moneyRegister) {
		super(name);
		this.moneyRegister=moneyRegister;
		this.currentTick=new AtomicInteger(0);

	}

	@Override
	protected void initialize() {


		//Subscribe to TickBroadcast
		subscribeBroadcast(TickBroadcast.class,message->
		{
			currentTick.set(message.getCurrentTick());
			System.out.println(getName() +" got the time :"+currentTick);
		});


		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message-> this.terminate());


		//Subscribe to BookOrderEvent
		subscribeEvent(BookOrderEvent.class, ev -> {
			OrderReceipt receipt=ev.getOrder();
			receipt.setProcessTick(currentTick.get());
			receipt.setSeller(getName());
			String bookTitle=ev.getOrder().getBookTitle();
			Customer customer=ev.getCustomer();
			Future<Integer> bookPriceFuture=sendEvent(new CheckAvailabilityEvent(bookTitle));
			int bookPrice=bookPriceFuture.get();
			receipt.setPrice(bookPrice);
			System.out.println(getName() +" got an answer an the book costs: "+bookPrice);
			if(bookPrice!=-1 && customer.getAvailableCreditAmount()>=bookPrice){
				System.out.println(getName() +"confirmed that the customer has enough money and sending to InventoryService");
				Future<OrderResult> acquireBookFuture=sendEvent(new AcquireBookEvent(bookTitle));
				OrderResult acquireBook=acquireBookFuture.get();
				if(acquireBook==OrderResult.SUCCESSFULLY_TAKEN){
					receipt.setIssuedTick(currentTick.get());
					moneyRegister.chargeCreditCard(customer,bookPrice);
					moneyRegister.file(receipt);
					complete(ev,receipt);
				}
				else{
					complete(ev,null);
				}
			}
			else
				complete(ev,null);
		});
	}
}
