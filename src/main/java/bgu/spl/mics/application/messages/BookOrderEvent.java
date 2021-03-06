package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
/**
 * an event that api service send to selling service in order to order a book
 * this message contains Customer info and not done order receipt
 */
public class BookOrderEvent implements Event<OrderReceipt> {
    private OrderReceipt order;
    private Customer customer;

    public BookOrderEvent(OrderReceipt orderReceipt,Customer customer) {
        this.order = orderReceipt;
        this.customer=customer;

    }

    public OrderReceipt getOrder() {
        return order;
    }

    public Customer getCustomer() {
        return customer;
    }
}

