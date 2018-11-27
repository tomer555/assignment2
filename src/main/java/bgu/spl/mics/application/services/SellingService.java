package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.AcquireBookEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

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
public class SellingService extends MicroService{
	private MoneyRegister moneyRegister;
	private int currentTick;
	public SellingService(String name,MoneyRegister moneyRegister) {
		super(name);
		this.moneyRegister=moneyRegister;
	}

	@Override
	protected void initialize() {


		//Subscribe to TickBroadcast
		subscribeBroadcast(TickBroadcast.class,message->{


		});


		//Subscribe to BookOrderEvent
		subscribeEvent(BookOrderEvent.class, ev -> {
			String bookTitle=ev.getOrder().getBookTitle();
			Customer customer=ev.getCustomer();
			Future<Integer> bookPriceFuture=sendEvent(new CheckAvailabilityEvent(bookTitle));
			int bookPrice=bookPriceFuture.get();

			if(bookPrice!=-1 && customer.getAvailableCreditAmount()>=bookPrice){
				moneyRegister.chargeCreditCard(customer,bookPrice);
				Future<OrderResult> acquireBookFuture=sendEvent(new AcquireBookEvent(bookTitle));
				OrderResult acquireBook=acquireBookFuture.get();
				if(acquireBook==OrderResult.SUCCESSFULLY_TAKEN){
					//should make order receipt and complete
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
