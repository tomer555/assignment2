package bgu.spl.mics;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static volatile MessageBus instance=null;
	private static Object o =new Object();
	private HashMap<MicroService,Queue<Message>> queueMap;

//A Thread safe constructor
	private MessageBusImpl(){}
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

	   	// TODO Auto-generated method stub
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
