package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireBookEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.io.Serializable;


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
	private boolean initialized;

	public InventoryService(String name, Inventory inventory) {
		super(name);
		this.inventory = inventory;
		this.initialized=false;
	}

	@Override
	protected void initialize() {


		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->this.terminate());

		subscribeEvent(CheckAvailabilityEvent.class, (ev) -> {


			System.out.println(getName()+ " got CheckAvailabilityEvent of book: "+ev.getBookTitle()+" to check");
			Integer bookPrice = inventory.checkAvailabilityAndGetPrice(ev.getBookTitle());
			complete(ev, bookPrice);
		});

		subscribeEvent(AcquireBookEvent.class,ev->{
			System.out.println(getName()+ " got AcquireBookEvent of book: "+ev.getBookTitle()+" to acquire");
			OrderResult bookTaken =inventory.take(ev.getBookTitle());
			complete(ev,bookTaken);
		});
		initialized=true;
	}

	public boolean isInitialized() {
		return initialized;
	}
}