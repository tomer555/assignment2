package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
/**
 * an event that selling service send to inventory service to check if a
 * certain book is available, this message contains the book title
 */
public class CheckAvailabilityEvent implements Event<Integer> {
    private String bookTitle;

    public CheckAvailabilityEvent(String bookTitle){
        this.bookTitle=bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }


}