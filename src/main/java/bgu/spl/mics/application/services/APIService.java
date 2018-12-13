package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService implements Serializable {
	private List<OrderReceipt> orderSchedule;
	private Customer customer;
	private int currentTick;
	private int TickToSend;
	private int index;
    private final CountDownLatch startSignal;
    private final CountDownLatch endSignal;


	public APIService(String name, List<OrderReceipt> orderSchedule, Customer customer,CountDownLatch startSignal,CountDownLatch endSignal) {
		super(name);
		this.orderSchedule=orderSchedule;
		this.customer=customer;
		this.currentTick=0;
		this.TickToSend=0;
		this.index=0;
        this.startSignal=startSignal;
        this.endSignal=endSignal;
		//sorting list from smallest Order tick to largest
		orderSchedule.sort(Comparator.comparingInt(OrderReceipt::getOrderTick));

		if(!orderSchedule.isEmpty()){
			TickToSend=orderSchedule.get(0).getOrderTick();
		}
		customer.setCustomerReceiptList(orderSchedule);
	}



	@Override
	protected void initialize() {
		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message-> {
            this.terminate();
            endSignal.countDown();
        });

		//Subscribe to TickBroadcast
		subscribeBroadcast(TickBroadcast.class, message->{
			currentTick=message.getCurrentTick();
			while (index<orderSchedule.size() && currentTick==TickToSend){
				OrderReceipt orderReceipt=orderSchedule.get(index);
				sendEvent(new BookOrderEvent(orderReceipt, customer));
				if(++index<orderSchedule.size())
					TickToSend=orderSchedule.get(index).getOrderTick();
			}
		});
        startSignal.countDown();
	}
}

