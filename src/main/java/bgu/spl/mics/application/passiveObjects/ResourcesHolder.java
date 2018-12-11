package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Future;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
	private static volatile ResourcesHolder instance = null;
	private static final Object lockResource = new Object();
	private Queue<DeliveryVehicle> carsQueue;
	private Queue<Future<DeliveryVehicle>> futuresToResolve;

	private ResourcesHolder(){
		this.carsQueue=new LinkedList<>();
		this.futuresToResolve=new LinkedList<>();
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static ResourcesHolder getInstance() {
		ResourcesHolder result = instance;
		if (result == null) {
			synchronized (lockResource) {
				result = instance;
				if (result == null)
					instance = result = new ResourcesHolder();
			}
		}
		return result;
	}


	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public synchronized Future<DeliveryVehicle> acquireVehicle() {
		DeliveryVehicle vehicle = carsQueue.poll();
		Future<DeliveryVehicle> future = new Future<>();
		if(vehicle!=null) {
			future.resolve(vehicle);
			return future;
		}
		futuresToResolve.add(future);
		return future;
	}
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public synchronized void releaseVehicle(DeliveryVehicle vehicle) {
		if(!futuresToResolve.isEmpty())
			futuresToResolve.poll().resolve(vehicle);
		else
			carsQueue.add(vehicle);
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		Collections.addAll(carsQueue,vehicles);
	}

}
