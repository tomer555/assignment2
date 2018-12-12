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
	private static final Object lockBus = new Object();
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> queueMap;
	private ConcurrentHashMap<Class<?>, Queue<MicroService>> messageSubscribersMap;
	private ConcurrentHashMap<Event, Future> futuresMap;


	//A Thread safe constructor
	private MessageBusImpl() {
		this.queueMap = new ConcurrentHashMap<>();
		this.messageSubscribersMap = new ConcurrentHashMap<>();
		this.futuresMap = new ConcurrentHashMap<>();
	}

	/**
	 * @return Singleton MessageBus
	 */
	public static MessageBus getInstance() {
		MessageBus result = instance;
		if (result == null) {
			synchronized (lockBus) {
				result = instance;
				if (result == null)
					instance = result = new MessageBusImpl();
			}
		}
		return result;
	}


	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private void subscribeMessage(Class<? extends Message> type,MicroService m) {
		synchronized (type) {
				if (messageSubscribersMap.containsKey(type))
					messageSubscribersMap.get(type).add(m);
				else {
					Queue<MicroService> toInsert = new LinkedList<>();
					toInsert.add(m);
					messageSubscribersMap.put(type, toInsert);
				}
			}
		}



	/**
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @param <T>  Generic type
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		subscribeMessage(type,m);
	}

	/**
	 * @param type The type to subscribe to.
	 * @param m    The subscribing micro-service.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		subscribeMessage(type,m);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future<T> future = (Future<T>) futuresMap.get(e);
		future.resolve(result);
	}


	/**
	 * @param b The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		if (!messageSubscribersMap.containsKey(b.getClass()))
			return;
		synchronized (messageSubscribersMap.get(b.getClass())) {
			Queue<MicroService> microServices = messageSubscribersMap.get(b.getClass());
			for (MicroService m : microServices) {
				if (m != null) {
					try {
						queueMap.get(m).put(b);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}



	/**
	 * @param e   The event to add to the queue.
	 * @param <T> generic type
	 * @return future from type T that will be resolved at some point
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if (!messageSubscribersMap.containsKey(e.getClass()))
			return null;
		Queue<MicroService> microServices = messageSubscribersMap.get(e.getClass());
		Future<T> future = new Future<>();
		futuresMap.put(e, future);
		synchronized (messageSubscribersMap.get(e.getClass())) {
			if (!messageSubscribersMap.get(e.getClass()).isEmpty()) {
					MicroService round = microServices.poll();
					if(round!=null) {
						microServices.add(round);
						queueMap.get(round).add(e);
					}
					else
						return null;
			}
			else
				return null;
		}
		return future;
	}

	/**
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void register(MicroService m) {
		queueMap.put(m, new LinkedBlockingQueue<>());
	}

	/**
	 * @param m the micro-service to unregister.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void unregister(MicroService m) {
		messageSubscribersMap.forEach((message, queue) -> {
			synchronized (messageSubscribersMap.get(message)) {
				if(queue!=null)
					queue.removeIf(m::equals);
			}
		});
		/*clear all the messages in the Micro-Service queue
		  resolving all the futures to null and deleting the queue
		 */
		synchronized (queueMap.get(m)) {
			queueMap.get(m).forEach((message -> {
				if(message instanceof Event)
					futuresMap.get(message).resolve(null);
			}));
			queueMap.remove(m);
		}
	}

	/**
	 * @param m The micro-service requesting to take a message from its message
	 *          queue.
	 * @return Message that will return to a microService to execute
	 * @throws InterruptedException if the Thread will be interrupted
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (!queueMap.containsKey(m))
			throw new InterruptedException();
		BlockingQueue<Message> microMessages = queueMap.get(m);
		return microMessages.take();
	}
}

