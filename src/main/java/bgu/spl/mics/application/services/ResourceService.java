package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireCarEvent;
import bgu.spl.mics.application.messages.ReturnCarEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

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
	private final CountDownLatch startSignal;
	private final CountDownLatch endSignal;

	public ResourceService(String name,ResourcesHolder resourcesHolder,CountDownLatch startSignal,CountDownLatch endSignal) {
		super(name);
		this.resourcesHolder=resourcesHolder;
		this.startSignal=startSignal;
		this.endSignal=endSignal;
	}

	@Override
	protected void initialize() {


		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->{
			this.terminate();
			endSignal.countDown();
			System.out.println(getName() +" is terminated and endSignal on: "+endSignal.getCount());
		});


		subscribeEvent(AcquireCarEvent.class,ev->{
			System.out.println(getName() + " got a demand for a Vehicle");
			Future <DeliveryVehicle> deliveryVehicleFuture=resourcesHolder.acquireVehicle();
				System.out.println(getName()+ " Returned a Future for Vehicle");
				complete(ev,deliveryVehicleFuture);
			});



		subscribeEvent(ReturnCarEvent.class,ev->{
			resourcesHolder.releaseVehicle(ev.getCarToReturn());
			System.out.println(getName() +" got back car: "+ev.getCarToReturn().getLicense()+" and returned it");
		});
		startSignal.countDown();
	}
}
