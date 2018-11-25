
import java.lang.reflect.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import bgu.spl.mics.*;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleBroadcastListenerService;
import bgu.spl.mics.example.services.ExampleEventHandlerService;
import bgu.spl.mics.example.services.ExampleMessageSenderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * This is a Unit Test for the {@link MessageBus} interface.
 *
 * */

public class MessageBusTest {
    //Test Members
    private MessageBus bus;
    private MicroService broadcastListener;
    private MicroService eventHandler;
    private MicroService messageHandler;
    private Field queueMap;
    private Field messageSubscribersMap;
    private Field futuresMap;




    @Before
    public void setUp() throws NoSuchFieldException, ClassNotFoundException {
        this.bus = createBus();//Create MessageBus

        //Create MicroServices
        this.broadcastListener=new ExampleBroadcastListenerService("brod0",new String[] {"2"});
        this.eventHandler=new ExampleEventHandlerService("event1",new String[] {"1"});
        this.messageHandler=new ExampleMessageSenderService("message1",new String[] {"event"});


        //Initialize private MessageBus fields
        InitializeFields();
    }
    protected void InitializeFields() throws ClassNotFoundException, NoSuchFieldException {
        this.queueMap=Class.forName("bgu.spl.mics.MessageBusImpl").getDeclaredField("queueMap");
        this.messageSubscribersMap=Class.forName("bgu.spl.mics.MessageBusImpl").getDeclaredField("messageSubscribersMap");
        this.futuresMap=Class.forName("bgu.spl.mics.MessageBusImpl").getDeclaredField("futuresMap");
    }

    protected MessageBus createBus() {
        return MessageBusImpl.getInstance();
    }
    /**
     * This is a Unit Test for the {@link MessageBus#subscribeEvent(Class, MicroService)} interface.
     *
     * */
    @Test
    public void subscribeEvent() {
        bus.subscribeEvent(ExampleEvent.class,messageHandler);
        bus.subscribeEvent(ExampleEvent.class,eventHandler);
        try {
            messageSubscribersMap.setAccessible(true);
            ConcurrentHashMap hashMap = (ConcurrentHashMap)messageSubscribersMap.get(bus);
            Assert.assertTrue(hashMap.containsKey(ExampleEvent.class));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    /**
     * This is a Unit Test for the {@link MessageBus#subscribeBroadcast(Class, MicroService)} interface.
     *
     * */
    @Test
    public void subscribeBroadcast() {
        bus.subscribeBroadcast(ExampleBroadcast.class,broadcastListener);
        try {
            messageSubscribersMap.setAccessible(true);
            ConcurrentHashMap hashMap = (ConcurrentHashMap)messageSubscribersMap.get(bus);
            Assert.assertTrue(hashMap.containsKey(ExampleBroadcast.class));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
    /**
     * This is a Unit Test for the {@link MessageBus#complete(Event, Object)} interface.
     *
     * */
    @Test
    public void complete() {
        Future<Integer> future=new Future<>();
        try {
            futuresMap.setAccessible(true);
            ConcurrentHashMap hashMap = (ConcurrentHashMap)futuresMap.get(bus);
            Message m1=new ExampleEvent("sender1");
            //hashMap.put(m1,future); warning!!!
            Integer i=1;
            //bus.complete(m1,i);  //compilition error
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void sendBroadcast() {

        MicroService m1=new ExampleBroadcastListenerService("brod1",new String[] {"2"});
        MicroService m2=new ExampleBroadcastListenerService("brod2",new String[] {"2"});
        bus.register(m1);
        bus.register(m2);
        bus.subscribeBroadcast(ExampleBroadcast.class,m1);
        bus.subscribeBroadcast(ExampleBroadcast.class,m2);

        Broadcast brodcast=new ExampleBroadcast("test");
        bus.sendBroadcast(brodcast);
        try {
            queueMap.setAccessible(true);
            ConcurrentHashMap hashMap = (ConcurrentHashMap)queueMap.get(bus);
            Queue queue1= (Queue) hashMap.get(m1);
            Queue queue2= (Queue) hashMap.get(m2);
            Assert.assertTrue(queue1.contains(brodcast));
            Assert.assertTrue(queue2.contains(brodcast));

        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }







    /**
     * This is a Unit Test for the {@link MessageBus#sendEvent(Event)} interface.
     *
     * */
    @Test
    public void sendEvent() {
       bus.register(messageHandler);
       bus.subscribeEvent(ExampleEvent.class,messageHandler);
       Event<String> m=new ExampleEvent("test") ;
       bus.sendEvent(m);
        try {
            queueMap.setAccessible(true);
            ConcurrentHashMap hashMap = (ConcurrentHashMap)queueMap.get(bus);
            Queue queue= (Queue) hashMap.get(messageHandler);
            Assert.assertTrue(queue.contains(m));

        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    /**
     * This is a Unit Test for the {@link MessageBus#register(MicroService)} interface.
     *
     * */
    @Test
    public void register() {
        bus.register(broadcastListener);
        bus.register(eventHandler);
        bus.register(messageHandler);
        try {
          queueMap.setAccessible(true);
          ConcurrentHashMap hashMap = (ConcurrentHashMap)queueMap.get(bus);
            Assert.assertTrue(hashMap.containsKey(broadcastListener));
            Assert.assertTrue(hashMap.containsKey(eventHandler));
            Assert.assertTrue(hashMap.containsKey(messageHandler));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
    /**
     * This is a Unit Test for the {@link MessageBus#unregister(MicroService)} interface.
     *
     * */
    @Test
    public void unregister() {
        register();
        bus.unregister(broadcastListener);
        bus.unregister(eventHandler);
        bus.unregister(messageHandler);
        try {
            queueMap.setAccessible(true);
            ConcurrentHashMap hashMap = (ConcurrentHashMap)queueMap.get(bus);
            Assert.assertFalse(hashMap.containsKey(broadcastListener));
            Assert.assertFalse(hashMap.containsKey(eventHandler));
            Assert.assertFalse(hashMap.containsKey(messageHandler));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void awaitMessage() {
        MicroService m2=new ExampleBroadcastListenerService("brod2",new String[] {"2"});
        bus.register(m2);
        bus.subscribeBroadcast(ExampleBroadcast.class,m2);
        AtomicReference<Message> message=new AtomicReference<>();
        Thread t1=new Thread(()-> {
            try {
                message.set(bus.awaitMessage(m2));
            } catch (InterruptedException e) {
                fail(e.getMessage());
            }
        });
        t1.start();
        Broadcast brodcast=new ExampleBroadcast("test");
        bus.sendBroadcast(brodcast);
        Assert.assertEquals(brodcast,message.get());
    }
}