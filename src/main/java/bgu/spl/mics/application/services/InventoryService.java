package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireBookEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService implements Serializable {
	private Inventory inventory;
	private final CountDownLatch startSignal;
	private final CountDownLatch endSignal;

	public InventoryService(String name, Inventory inventory,CountDownLatch startSignal,CountDownLatch endSignal) {
		super(name);
		this.inventory = inventory;
		this.startSignal=startSignal;
		this.endSignal=endSignal;
	}

	@Override
	protected void initialize() {

		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->{
			this.terminate();
			endSignal.countDown();
		});

		/*
			Subscribing to CheckAvailabilityEvent - checks if book is available and returns its price
			if not exists or out of stock will return -1
		 */
		subscribeEvent(CheckAvailabilityEvent.class, (ev) -> {
			System.out.println(getName()+ " got CheckAvailabilityEvent of book: "+ev.getBookTitle()+" to check");
			Integer bookPrice = inventory.checkAvailabilityAndGetPrice(ev.getBookTitle());
			complete(ev, bookPrice);
		});

		/*
			Subscribing to AcquireBookEvent - tries to acquire the given book,
			if succeeds will return SUCCESSFULLY_TAKEN, else will return NOT_IN_STOCK
		 */
		subscribeEvent(AcquireBookEvent.class,ev->{
			System.out.println(getName()+ " got AcquireBookEvent of book: "+ev.getBookTitle()+" to acquire");
			OrderResult bookTaken =inventory.take(ev.getBookTitle());
			complete(ev,bookTaken);
		});
		startSignal.countDown();
	}
}