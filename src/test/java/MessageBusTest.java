
import static org.junit.Assert.*;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import org.junit.Before;
import org.junit.Test;
/**
 * This is a Unit Test for the {@link MessageBus} interface.
 *
 * */


public class MessageBusTest {
    private MessageBus bus;

    @Before
    protected void setUp() {
        this.bus = createBus();
    }


    protected MessageBus createBus() {
        return MessageBusImpl.getInstance();
    }
    /**
     * This is a Unit Test for the {@link MessageBus#subscribeEvent} interface.
     *
     * */
    @Test
    public void subscribeEvent() {
    }

    @Test
    public void subscribeBroadcast() {
    }

    @Test
    public void complete() {
    }

    @Test
    public void sendBroadcast() {
    }

    @Test
    public void sendEvent() {
    }

    @Test
    public void register() {
    }

    @Test
    public void unregister() {
    }

    @Test
    public void awaitMessage() {
    }
}