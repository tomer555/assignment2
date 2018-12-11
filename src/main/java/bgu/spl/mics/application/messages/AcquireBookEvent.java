package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.OrderResult;

/**
 * an event that a certain Selling Service will send to a certain Inventory Service
 * in order to acquire a specific book. this event will result a future that will hold
 * the status of the acquisition.
 */
public class AcquireBookEvent implements Event<OrderResult> {
    private String bookTitle;

    public AcquireBookEvent(String bookTitle){
        this.bookTitle=bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }

}


