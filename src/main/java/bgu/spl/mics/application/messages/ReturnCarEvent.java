package bgu.spl.mics.application.messages;


import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

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
