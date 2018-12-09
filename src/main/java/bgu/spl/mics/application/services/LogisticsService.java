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

	public LogisticsService(String name) {
		super(name);

	}

	@Override
	protected void initialize() {

		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->this.terminate());


		subscribeEvent(DeliveryEvent.class,ev->{
			System.out.println(getName()+" got new Delivery to customer: "+ev.getCustomer().getName());
			//getting a reference to the messageBus's future
			System.out.println(getName()+"asking Recourse Service to acquire a car");
			Future<DeliveryVehicle> deliveryEventFuture=sendEvent(new AcquireCarEvent());
			//waiting for that future to resolve
			DeliveryVehicle car= deliveryEventFuture.get();
			System.out.println(getName() + " got the car to deliver: "+car.getLicense());
			Customer customer=ev.getCustomer();

			car.deliver(customer.getAddress(), customer.getDistance());
			System.out.println(getName() +" confirmed that car: "+car.getLicense()+" delivered the book to: "+ev.getCustomer().getAddress());
			System.out.println(getName()+" returning car to Resource Service");
			sendEvent(new ReturnCarEvent(car));

		});
	}
}
