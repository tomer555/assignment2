package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;

/**
 * Broadcast that contains the current Tick in the system
 */
public class TickBroadcast implements Broadcast {

    private int currentTick;


    public TickBroadcast(int currentTick) {
        this.currentTick = currentTick;

    }

    public int getCurrentTick() {
        return currentTick;
    }

}

