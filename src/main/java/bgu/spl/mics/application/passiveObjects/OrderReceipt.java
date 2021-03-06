package bgu.spl.mics.application.passiveObjects;
import java.io.Serializable;

/**
 * Passive data-object representing a receipt that should 
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt implements Serializable {
	private int orderId;
	private String seller;
	private int customerId;
	private String bookTitle;
	private int price;
	private int issuedTick;
	private int orderTick;
	private int processTick;

	public OrderReceipt(int orderId,int customerId,String bookTitle,int orderTick){
		this.orderId=orderId;
		this.customerId=customerId;
		this.bookTitle=bookTitle;
		this.orderTick=orderTick;
	}
	/**
     * Retrieves the orderId of this receipt.
     */
	public int getOrderId() { return orderId; }
	
	/**
     * Retrieves the name of the selling service which handled the order.
     */
	public String getSeller() { return seller;}


	public void setSeller(String seller){
		this.seller=seller;
	}
	/**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     * @return the ID of the customer
     */
	public int getCustomerId() { return customerId;}
	
	/**
     * Retrieves the name of the book which was bought.
     */
	public String getBookTitle() { return bookTitle;}
	
	/**
     * Retrieves the price the customer paid for the book.
     */
	public int getPrice() { return price;}
	
	/**
     * Retrieves the tick in which this receipt was issued.
     */
	public int getIssuedTick() { return issuedTick;}

	public void setIssuedTick(int issuedTick) {
		this.issuedTick=issuedTick;
	}
	
	/**
     * Retrieves the tick in which the customer sent the purchase request.
     */
	public int getOrderTick() { return orderTick;}


	public void setOrderTick(int orderTick) {
		this.orderTick=orderTick;
	}
	
	/**
     * Retrieves the tick in which the treating selling service started 
     * processing the order.
     */
	public int getProcessTick() { return processTick;}


	public void setProcessTick(int processTick) {
		this.processTick=processTick;
	}

	public void setPrice(int price){
		this.price=price;
	}

	public void setOrderId(int orderId){
		this.orderTick=orderId;
	}

	public String toString(){
		String output;
		output="Order ID: "+orderId+"\n"+"Customer ID: "+customerId+"\n"+"Seller"+seller+"\n"+"Book Name: "+bookTitle+"\n";
		output=output+"Book Price "+price+"\n"+"orderTick: "+orderTick+"\n"+"processTick: "+processTick+"\n"+"issuedTick: "+issuedTick+"\n";
		return output;
	}


}
