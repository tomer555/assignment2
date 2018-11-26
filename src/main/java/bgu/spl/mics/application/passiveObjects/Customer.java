package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer {
	private String name;
	private int id;
	private String address;
	private int distance;
	private int creditNumber;
	private List<OrderReceipt> orderReceipts;
	private AtomicInteger availableCreditAmount;


	public Customer(String name,int id, String address,int distance, int creditNumber, int availableCreditAmount){
		this.name=name;
		this.id=id;
		this.address=address;
		this.distance=distance;
		this.creditNumber=creditNumber;
		this.orderReceipts=new LinkedList<>();
		this.availableCreditAmount=new AtomicInteger(availableCreditAmount);
	}
	/**
     * Retrieves the name of the customer.
     */
	public String getName() { return name; }

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() { return id; }
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() { return address; }
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() { return distance;}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() { return orderReceipts ; }
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() { return availableCreditAmount.get(); }


	/**
	 * charge a certain customer and decrease @toCharge from his available Credit Amount
	 * @param toCharge
	 */
	public void setAvailableCreditAmount(int toCharge){
		availableCreditAmount.set(availableCreditAmount.intValue()-toCharge);
	}

	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() { return creditNumber; }
	
}
