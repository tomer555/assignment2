package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.OrderResult;

public class CheckAvilabilityEvent implements Event<OrderResult> {
    private String senderName;

    CheckAvilabilityEvent(String senderName){
        this.senderName=senderName;
    }

}
