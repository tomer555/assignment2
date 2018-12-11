package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class DeliveryEvent implements Event<Future<DeliveryVehicle>> {
    private Customer customer;
    public DeliveryEvent(Customer customer){
        this.customer=customer;
    }

    public DeliveryEvent() {

    }

    public Customer getCustomer() {
        return customer;
    }
}
