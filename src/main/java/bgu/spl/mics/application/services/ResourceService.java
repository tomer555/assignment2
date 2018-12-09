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
	private AtomicInteger currentTick;
	private ConcurrentHashMap<AcquireCarEvent,Future<DeliveryVehicle>> futures;
	public ResourceService(String name,ResourcesHolder resourcesHolder) {
		super(name);
		this.resourcesHolder=resourcesHolder;
		this.currentTick=new AtomicInteger(0);
		this.futures=new ConcurrentHashMap<>();
	}

	@Override
	protected void initialize() {



		//Subscribe to TickBroadcast
		subscribeBroadcast(TickBroadcast.class, message->
		{
			currentTick.set(message.getCurrentTick());
			System.out.println(getName() +" got the time :"+currentTick);

			futures.forEach((key,value)->{
				if(value.isDone()) {
					futures.remove(key);
					complete(key,value.get());
				}
			});

		});
		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->this.terminate());


		subscribeEvent(AcquireCarEvent.class,ev->{
			System.out.println(getName() + "got a demand for a Vehicle");
			Future <DeliveryVehicle> deliveryVehicleFuture=resourcesHolder.acquireVehicle();
			if(deliveryVehicleFuture.isDone()){
				System.out.println(getName()+ " Found a Car!!");
				complete(ev,deliveryVehicleFuture.get());
			}
			else
				futures.put(ev,deliveryVehicleFuture);
			//that one will change my first future from the logistic. we have created 2 futures. resourceholder(GC AUTO) and messagebus(complete)

		});

		subscribeEvent(ReturnCarEvent.class,ev->{

			resourcesHolder.releaseVehicle(ev.getCarToReturn());
			System.out.println(getName() +" got back car: "+ev.getCarToReturn().getLicense()+" got returned");
		});

	}
}
