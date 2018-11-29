package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireCarEvent;
import bgu.spl.mics.application.messages.ReturnCarEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.io.Serializable;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService implements Serializable {
	private ResourcesHolder resourcesHolder;
	private boolean initialized;
	public ResourceService(String name,ResourcesHolder resourcesHolder) {
		super(name);
		this.resourcesHolder=resourcesHolder;
		this.initialized =false;

	}

	@Override
	protected void initialize() {

		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->this.terminate());


		subscribeEvent(AcquireCarEvent.class,ev->{
			System.out.println(getName() + "got a demand for a Vehicle");
			Future <DeliveryVehicle> deliveryVehicleFuture=resourcesHolder.acquireVehicle();
			DeliveryVehicle deliveryVehicle= deliveryVehicleFuture.get();
			System.out.println(getName()+ " Found a Car!!");
			//that one will change my first future from the logistic. we have created 2 futures. resourceholder(GC AUTO) and messagebus(complete)
			complete(ev,deliveryVehicle);
		});

		subscribeEvent(ReturnCarEvent.class,ev->{

			resourcesHolder.releaseVehicle(ev.getCarToReturn());
			System.out.println(getName() +" got back car: "+ev.getCarToReturn().getLicense()+" got returned");
		});

		initialized=true;

	}

	public boolean isInitialized() {
		return initialized;
	}
}
