package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.passiveObjects.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

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
	private List<Future<OrderReceipt>> orderReceiptFutures;
	private BlockingQueue<Future<OrderReceipt>> doneReceiptFutures;
	private Customer customer;
	public APIService(String name, List<OrderReceipt> orderSchedule, Customer customer) {
		super(name);
		this.orderSchedule=orderSchedule;
		this.orderReceiptFutures=new LinkedList<>();
		this.doneReceiptFutures=new LinkedBlockingQueue<>();
		this.customer=customer;
	}

	@Override
	protected void initialize() {
		orderSchedule.sort((order1,order2)->{
			if (order1.getOrderTick()<order2.getOrderTick())
				return 1;
			else
				return 0;
		});
		for (OrderReceipt order:orderSchedule) {
			Future<OrderReceipt> orderFuture = sendEvent(new BookOrderEvent(order, customer));
			orderReceiptFutures.add(orderFuture);
		}
		Thread doneFutures=new Thread(()->{
			while (!orderReceiptFutures.isEmpty()){
				Stream<Future<OrderReceipt>> stream=orderReceiptFutures.stream();
				stream.filter(Future::isDone).forEach((future)->{
					doneReceiptFutures.add(future);
					orderReceiptFutures.remove(future);
				});
			}
		});

		doneFutures.start();
		while (!orderReceiptFutures.isEmpty()){
			Future<OrderReceipt> readyReceipt= doneReceiptFutures.poll();
			if(readyReceipt!=null) {
				OrderReceipt receipt = readyReceipt.get();
				if (receipt != null)
					sendEvent(new DeliveryEvent(customer));
			}
		}







		
	}

}
