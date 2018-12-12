package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

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
	private final CountDownLatch startSignal;
	private final CountDownLatch endSignal;
	private AtomicInteger currentTick;

	public LogisticsService(String name,CountDownLatch startSignal,CountDownLatch endSignal) {
		super(name);
		this.startSignal=startSignal;
		this.endSignal=endSignal;
		this.currentTick=new AtomicInteger(0);

	}

	@Override
	protected void initialize() {


		//Subscribe to TickBroadcast
		subscribeBroadcast(TickBroadcast.class,message->
		{
			currentTick.set(message.getCurrentTick());
			System.out.println(getName() +" time :"+currentTick);

		});

		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->{
			this.terminate();
			endSignal.countDown();
			System.out.println(getName() +" is terminated | endSignal: "+endSignal.getCount());
		});


		//Subscribe To DeliveryEvent
		subscribeEvent(DeliveryEvent.class,ev->{
			System.out.println(getName() + " got new Delivery to customer: " + ev.getCustomer().getName());
			System.out.println(getName() + " asking Recourse Service to acquire a car");
			Future<Future<DeliveryVehicle>> doubleDeliveryEventFuture = sendEvent(new AcquireCarEvent(ev.getCustomer()));
			if(doubleDeliveryEventFuture!=null && doubleDeliveryEventFuture.get()!=null &&doubleDeliveryEventFuture.get().get()!=null) {
				DeliveryVehicle car=doubleDeliveryEventFuture.get().get();
				System.out.println(getName() + " got the car to deliver: " + car.getLicense());
				Customer customer = ev.getCustomer();
				car.deliver(customer.getAddress(), customer.getDistance());
				System.out.println(getName() + " confirmed that car: " + car.getLicense() + " delivered the book to: " + ev.getCustomer().getAddress());
				System.out.println(getName() + " returning car to Resource Service");
				sendEvent(new ReturnCarEvent(ev.getCustomer(), car));
				}
		});
		startSignal.countDown();
	}
}
