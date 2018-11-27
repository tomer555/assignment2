package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.OrderResult;

public class AcquireBookEvent implements Event<OrderResult> {
    private String bookTitle;

    public AcquireBookEvent(String bookTitle){
        this.bookTitle=bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}


