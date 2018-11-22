package bgu.spl.mics;

import jdk.internal.net.http.common.Pair;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static volatile MessageBus instance=null;
	private static Object o =new Object();
	private ConcurrentHashMap<MicroService,Queue<Message>> queueMap;
	//private ConcurrentHashMap< Class<? extends Event<?>>,Queue<MicroService>> eventSubscribersMap;
	private ConcurrentHashMap< Class<?>,Queue<MicroService>> eventSubscribersMap;
	private ConcurrentHashMap< Class<?>,Queue<MicroService>> broadcastSubscribersMap;
	//private myVector<Pair<? extends Message, BlockingQueue<MicroService>>> messageSubscribers;


//A Thread safe constructor
	private MessageBusImpl(){
		this.queueMap=new ConcurrentHashMap<>();
		this.eventSubscribersMap=new ConcurrentHashMap<>();
		this.broadcastSubscribersMap=new ConcurrentHashMap<>();

		//messageSubscribers= new myVector<>();


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
		boolean found= false;
		for(Map.Entry< Class<?>,Queue<MicroService>> message : eventSubscribersMap.entrySet()){
			if(type.isInstance(message.getKey())){
				message.getValue().add(m);
				found=true;
				break;
			}
		}
		if (!found)
		{
			BlockingQueue<MicroService> toInsert=new LinkedBlockingQueue<>();
			toInsert.add(m);
			eventSubscribersMap.put(type,toInsert);

		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		boolean found= false;
		for(Map.Entry< Class<?>,Queue<MicroService>> message : broadcastSubscribersMap.entrySet()){
			if(type.isInstance(message.getKey())){
				message.getValue().add(m);
				found=true;
				break;
			}
		}
		if (!found)
		{
			BlockingQueue<MicroService> toInsert=new LinkedBlockingQueue<>();
			toInsert.add(m);
			broadcastSubscribersMap.put(type,toInsert);

		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if(!broadcastSubscribersMap.containsKey(b.getClass()))
			return;

		Queue<MicroService>  microServices = broadcastSubscribersMap.get(b.getClass());
		for (MicroService m:microServices) {
			if (m != null) {
				microServices.add(m);
				queueMap.get(m).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if(!eventSubscribersMap.containsKey(e.getClass()))
			return null;

		Queue<MicroService>  microServices = eventSubscribersMap.get(e.getClass());
		MicroService round= microServices.poll();
		if (round==null)
			return null;
		microServices.add(round);
		queueMap.get(round).add(e);

		return null;
	}

	@Override
	public void register(MicroService m) {
		queueMap.put(m,new LinkedBlockingQueue<>());

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
