package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	private boolean resolved;
	private T result;
	private final Object resultLock;

	
	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		resolved=false;
		result=null;
		resultLock=new Object();
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
	 * @pre: none
	 * @post: resolved==true
	 *
     * 	       
     */

	public T get() {
		if (resolved)
		    return result;
		while (!resolved){
			synchronized (resultLock) {
				try {
					resultLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        }
        return result;
	}
	
	/**
     * Resolves the result of this Future object.
	 * @pre: resolved==false
	 * @pre: result==null
	 * @post: resolved==true
	 * @post: get()==result
	 *
     */
	public void resolve (T result) {
        synchronized (resultLock) {
            this.result = result;
            resolved = true;
            resultLock.notifyAll();
            }
        }

	
	/**
     * @return true if this object has been resolved, false otherwise
	 * @pre none
	 * @post none
     */
	public boolean isDone() {
		return resolved;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
   //  * @param timout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
	 *
	 * @pre: none
	 * @post: none
     */

	public T get(long timeout, TimeUnit unit) {
        long sleepMillis=unit.toMillis(timeout);
		if(resolved)
			return result;
		synchronized (resultLock) {
			try {
				resultLock.wait(sleepMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if(resolved)
			return result;
        else
            return null;
    }
}
