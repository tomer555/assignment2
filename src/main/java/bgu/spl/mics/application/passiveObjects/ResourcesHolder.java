package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
	private BlockingQueue<DeliveryVehicle> carsQueue;

	private ResourcesHolder(){
		this.carsQueue=new LinkedBlockingQueue<>();
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
	public Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> output = new Future<>();
		/*
		if() {//completely free car
			output.resolve();// someFreeCar inside
			//delete car
		}
		else
			return new Future<>();
			*/
	return null;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
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
