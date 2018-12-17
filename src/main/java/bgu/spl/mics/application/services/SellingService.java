package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
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
	private final CountDownLatch startSignal;
	private final CountDownLatch endSignal;

	public SellingService(String name, MoneyRegister moneyRegister, CountDownLatch startSignal,CountDownLatch endSignal) {
		super(name);
		this.moneyRegister=moneyRegister;
		this.currentTick=new AtomicInteger(0);
		this.startSignal=startSignal;
		this.endSignal=endSignal;

	}

	@Override

	protected void initialize() {

		//Subscribe to TickBroadcast
		subscribeBroadcast(TickBroadcast.class,message-> currentTick.set(message.getCurrentTick()));


		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message-> {
			this.terminate();
			endSignal.countDown();
		});


		//Subscribe to BookOrderEvent
		subscribeEvent(BookOrderEvent.class, ev -> {
			OrderReceipt receipt=ev.getOrder();
			receipt.setProcessTick(currentTick.get());
			receipt.setSeller(getName());
			String bookTitle=ev.getOrder().getBookTitle();
			Customer customer=ev.getCustomer();
			Future<Integer> bookPriceFuture=sendEvent(new CheckAvailabilityEvent(bookTitle));
			Integer bookPrice=-1;
			try {
				if (bookPriceFuture != null)
					bookPrice = bookPriceFuture.get();
				//sync on customer moneyLock to prevent situation when customer can be charged and get into minus
				synchronized (customer.getMoneyLock()) {
					if (bookPrice != -1 && customer.getAvailableCreditAmount() >= bookPrice) {
						receipt.setPrice(bookPrice);
						Future<OrderResult> acquireBookFuture = sendEvent(new AcquireBookEvent(bookTitle));
						if (acquireBookFuture != null && acquireBookFuture.get() != null && acquireBookFuture.get() == OrderResult.SUCCESSFULLY_TAKEN) {
							receipt.setIssuedTick(currentTick.get());
							moneyRegister.chargeCreditCard(customer, bookPrice);
							moneyRegister.file(receipt);
							complete(ev, receipt);
							sendEvent(new DeliveryEvent(customer));
						} else
							complete(ev, null);
					} else
						complete(ev, null);
				}
			}
			catch (NullPointerException e){
				complete(ev, null);
			}
		});
		startSignal.countDown();
	}
}
