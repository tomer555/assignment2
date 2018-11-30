package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;


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
	private Vector<Future<OrderReceipt>> orderReceiptFutures;
	private Customer customer;
	private int currentTick;
	private int TickToSend;
	private int index;


	public APIService(String name, List<OrderReceipt> orderSchedule, Customer customer) {
		super(name);
		this.orderSchedule=orderSchedule;
		this.orderReceiptFutures=new Vector<>();
		this.customer=customer;
		this.currentTick=0;
		this.TickToSend=0;
		this.index=0;
		//sorting list from smallest tick to last
		orderSchedule.sort((order1,order2)->{
			if (order1.getOrderTick()<order2.getOrderTick())
				return 1;
			else
				return 0;
		});
		if(!orderSchedule.isEmpty()){
			TickToSend=orderSchedule.get(0).getOrderTick();
		}
	}

	private void lastCall(){





	}

	@Override
	protected void initialize() {
		//Subscribe to TickBroadcast
		subscribeBroadcast(TickBroadcast.class, message->{
			currentTick=message.getCurrentTick();

			System.out.println(getName() +" got the time: "+currentTick);

			while (index<orderSchedule.size() && currentTick==TickToSend){
				OrderReceipt orderReceipt=orderSchedule.get(index);
				Future<OrderReceipt> orderFuture = sendEvent(new BookOrderEvent(orderReceipt, customer));
				orderReceiptFutures.add(orderFuture);
				if(++index<orderSchedule.size())
					TickToSend=orderSchedule.get(index).getOrderTick();
			}



				for(int i=0;i<orderReceiptFutures.size();i++){
                    Future<OrderReceipt> future =orderReceiptFutures.get(i);
				    if(future.isDone()) {
                        OrderReceipt receipt = future.get();
                        if(receipt!=null) {
                            System.out.println(getName()+" successfully got back the receipt of book: "+receipt.getBookTitle()+", Order tick: "+
                            receipt.getOrderTick()+", process Tick : "+receipt.getProcessTick()+", issued Tick: "+receipt.getIssuedTick());
                            sendEvent(new DeliveryEvent(customer));
                        }
                        orderReceiptFutures.remove(i);
                        i--;
                    }
                }
		});

		//Subscribe To Termination
		subscribeBroadcast(TerminationBroadcast.class, message->  this.terminate());

	}
}

