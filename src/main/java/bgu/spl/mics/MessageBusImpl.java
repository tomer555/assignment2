package bgu.spl.mics;

import jdk.internal.net.http.common.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static volatile MessageBus instance=null;
	private static Object o =new Object();
	private ConcurrentHashMap<MicroService,Queue<Message>> queueMap;
	private Vector<Pair<? extends Message,Queue<MicroService>>> messageSubscribers;


//A Thread safe constructor
	private MessageBusImpl(){
		this.queueMap=new ConcurrentHashMap<>();
		messageSubscribers=new myVector<>();


	}
	public static MessageBus getInstance(){
		MessageBus result= instance;
		if (result==null) {
			synchronized (o){
				result=instance;
				if (result==null)
					instance=result=new MessageBusImpl();
			}
		}
		return result;
	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

	   	for (Pair<? extends Message,Queue<MicroService>> pair :messageSubscribers){
	          if(type.isInstance(pair.first))    {
	          	pair.second.add(m);
			}
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(MicroService m) {
		queueMap.put(m,new PriorityBlockingQueue<>());

	}

	@Override
	public void unregister(MicroService m) {
	queueMap.remove(m);

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
