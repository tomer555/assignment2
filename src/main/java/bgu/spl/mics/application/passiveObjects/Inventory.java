package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;

//import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 * @inv:
 */
public class Inventory {

	/**
     * Retrieves the single instance of this class.

     */
	private List <BookInventoryInfo> listOfBooks;
	private static volatile Inventory instance = null;
	private static final Object lockInventory = new Object();


	//A Thread safe constructor
	private Inventory() {
		this.listOfBooks = new Vector<>();

	}
	public static Inventory getInstance() {
		Inventory result = instance;
		if (result == null) {
			synchronized (lockInventory) {
				result = instance;
				if (result == null)
					instance = result = new Inventory();
			}
		}
		return result;
	}
	
	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
	 * @pre:listOfBooks.isEmpty()==true
	 * @post:listOfBooks.isEmpty()==false
     */
	public void load (BookInventoryInfo[ ] inventory ) {

		Collections.addAll(this.listOfBooks,inventory);
	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
	 * @pre:listOfBooks.get(book).isAvailable==true
	 * @post:listOfBooks.get(book).capacity--
	 * @pre:listOfBooks.get(book).isAvailable==false
	 * @post:@return NOT_IN_STOCK
	*/
	public OrderResult take (String book) {
		
		return null;
	}
	
	
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
	 * @pre:none
	 * @post:none
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		//TODO: Implement this
		return -1;
	}
	
	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
	 * @pre:none
	 * @post:none
     */
	public void printInventoryToFile(String filename){
		//TODO: Implement this
	}
}
