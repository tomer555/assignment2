package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
/**
 * an event that api service send to logistic service in order to deliver a book,
 * this message contains Customer ifo for delivery purposes.
 */
public class DeliveryEvent implements Event<Future<DeliveryVehicle>> {
    private Customer customer;
    public DeliveryEvent(Customer customer){
        this.customer=customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
