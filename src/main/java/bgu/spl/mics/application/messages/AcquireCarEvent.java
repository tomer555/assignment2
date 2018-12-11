package bgu.spl.mics.application.messages;
import bgu.spl.mics.application.passiveObjects.Customer;
/**
 * this class extends DeliveryEvent because this message only holds the
 * customer info , but it differ than DeliveryEventby the class type.
 * therefore has another purpose: to actually acquire a car
 */
public class AcquireCarEvent extends DeliveryEvent {

    public AcquireCarEvent(Customer customer) {
        super(customer);
    }
}
