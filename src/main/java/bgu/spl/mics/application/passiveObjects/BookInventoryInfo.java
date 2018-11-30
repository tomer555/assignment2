package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo implements Serializable {
	private String bookTitle;
	private AtomicInteger amountInInventory;
	private int price;


	public BookInventoryInfo(String bookTitle,int amountInInventory,int price){
		this.bookTitle=bookTitle;
		this.amountInInventory=new AtomicInteger(amountInInventory);
		this.price=price;
	}
	/**
     * Retrieves the title of this book.
     * <p>
     * @return The title of this book.   
     */
	public String getBookTitle() { return bookTitle; }

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	public int getAmountInInventory() { return amountInInventory.get(); }

	public AtomicInteger getAtomicAmountInInventory() { return this.amountInInventory; }

	public void setAmountInInventory(int newAmount) {  amountInInventory.set(newAmount); }


	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	public int getPrice() { return price; }
	
	

	
}
