package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireCarEvent;
import bgu.spl.mics.application.messages.ReturnCarEvent;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.io.Serializable;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourceHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService implements Serializable {
	private ResourcesHolder resourcesHolder;
	public ResourceService(String name,ResourcesHolder resourcesHolder) {
		super(name);
		this.resourcesHolder=resourcesHolder;
	}

	@Override
	protected void initialize() {
		subscribeEvent(AcquireCarEvent.class,ev->{
			Future <DeliveryVehicle> deliveryVehicleFuture=resourcesHolder.acquireVehicle();
			DeliveryVehicle deliveryVehicle= deliveryVehicleFuture.get();
			complete(ev,deliveryVehicle);

		});

		subscribeEvent(ReturnCarEvent.class,ev->{
			resourcesHolder.releaseVehicle(ev.getCarToReturn());
		});

		
	}

}
