package bgu.spl.mics;

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

	private static volatile MessageBus instance = null;
	private static Object o = new Object();
	private ConcurrentHashMap<MicroService,Queue<Message>> queueMap;
	private ConcurrentHashMap<Class<?>, Queue<MicroService>> messageSubscribersMap;
	private ConcurrentHashMap<Event<?>, Future<?>> futuresMap;

	//private myVector<Pair<? extends Message, BlockingQueue<MicroService>>> messageSubscribers;


	//A Thread safe constructor
	private MessageBusImpl() {
		this.queueMap = new ConcurrentHashMap<>();
		this.messageSubscribersMap = new ConcurrentHashMap<>();
		this.futuresMap = new ConcurrentHashMap<>();

		//messageSubscribers= new myVector<>();
	}

	public static MessageBus getInstance() {
		MessageBus result = instance;
		if (result == null) {
			synchronized (o) {
				result = instance;
				if (result == null)
					instance = result = new MessageBusImpl();
			}
		}
		return result;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		boolean found = false;
		for (Map.Entry<Class<?>, Queue<MicroService>> message : messageSubscribersMap.entrySet()) {
			if (type.isInstance(message.getKey())) {
				message.getValue().add(m);
				found = true;
				break;
			}
		}
		if (!found) {
			BlockingQueue<MicroService> toInsert = new LinkedBlockingQueue<>();
			toInsert.add(m);
			messageSubscribersMap.put(type, toInsert);

		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		boolean found = false;
		for (Map.Entry<Class<?>, Queue<MicroService>> message : messageSubscribersMap.entrySet()) {
			if (type.isInstance(message.getKey())) {
				message.getValue().add(m);
				found = true;
				break;
			}
		}
		if (!found) {
			BlockingQueue<MicroService> toInsert = new LinkedBlockingQueue<>();
			toInsert.add(m);
			messageSubscribersMap.put(type, toInsert);

		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future future= futuresMap.get(e);
		future.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (!messageSubscribersMap.containsKey(b.getClass()))
			return;

		Queue<MicroService> microServices = messageSubscribersMap.get(b.getClass());
		for (MicroService m : microServices) {
			if (m != null) {
				microServices.add(m);
				queueMap.get(m).add(b);
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if (!messageSubscribersMap.containsKey(e.getClass()))
			return null;

		Queue<MicroService> microServices = messageSubscribersMap.get(e.getClass());
		MicroService round = microServices.poll();
		if (round == null)
			return null;
		microServices.add(round);
		queueMap.get(round).add(e);
		Future<T> future = new Future<>();
		futuresMap.put(e.getClass(), future);

		return future;
	}

	@Override
	public void register(MicroService m) {
		queueMap.put(m, new LinkedBlockingQueue<>());

	}

	@Override
	public void unregister(MicroService m) {
		queueMap.remove(m);

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (!queueMap.containsKey(m))
			throw new InterruptedException();
		Queue<Message> microMessages = queueMap.get(m);
		while (microMessages.isEmpty())
			queueMap.get(m).wait();
		return microMessages.poll();
	}
}
