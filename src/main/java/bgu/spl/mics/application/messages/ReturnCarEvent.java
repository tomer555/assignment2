package bgu.spl.mics.application.messages;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
/**
 * an event that logistic send to resource service upon finishing the delivery
 * to return the vehicle to resource service.
 * the future of this message will be ignored, the message purpose is to deliver back the car.
 */
public class ReturnCarEvent extends DeliveryEvent {
    private DeliveryVehicle carToReturn;

    public ReturnCarEvent(Customer customer,DeliveryVehicle carToReturn){
        super(customer);
        this.carToReturn=carToReturn;
    }

    public DeliveryVehicle getCarToReturn() {
        return carToReturn;
    }
}
