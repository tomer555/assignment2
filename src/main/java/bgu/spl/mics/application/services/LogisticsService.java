package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.io.Serializable;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService implements Serializable {
	private int currentTick;
	public LogisticsService(String name) {
		super(name);
		this.currentTick=1;

	}

	@Override
	protected void initialize() {

		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->this.terminate());


		subscribeEvent(DeliveryEvent.class,ev->{
			Future<DeliveryVehicle> deliveryEventFuture=sendEvent(new AcquireCarEvent());
			DeliveryVehicle car= deliveryEventFuture.get();
			Customer customer=ev.getCustomer();
			Thread driver =new Thread(()-> {
				car.deliver(customer.getAddress(), customer.getDistance());
				sendEvent(new ReturnCarEvent(car));
			});
			driver.start();


		});
		
	}

}
