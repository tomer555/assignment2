package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.application.Serialize;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {
	private static volatile MoneyRegister instance = null;
	private static transient final Object lockRegister = new Object();
	private List<OrderReceipt> orderReceiptList;
	private AtomicInteger earnings;
	private MoneyRegister(){
		this.earnings=new AtomicInteger(0);
		this.orderReceiptList=new LinkedList<>();
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static MoneyRegister getInstance() {
		MoneyRegister result = instance;
		if (result == null) {
			synchronized (lockRegister) {
				result = instance;
				if (result == null)
					instance = result = new MoneyRegister();
			}
		}
		return result;
	}

	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void file (OrderReceipt r) {
		orderReceiptList.add(r);
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {return earnings.get(); }
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {
		c.setAvailableCreditAmount(amount);
		earnings.set(earnings.intValue()+amount);
	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.. 
     */
	public void printOrderReceipts(String filename) {
		Serialize.serializeObject(filename, orderReceiptList);
	}
}
