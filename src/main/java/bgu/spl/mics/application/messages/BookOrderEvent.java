package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class BookOrderEvent implements Event<OrderReceipt> {
    private String senderName;
    BookOrderEvent(String senderName, Customer customer){
        this.senderName=senderName;

    }



}
