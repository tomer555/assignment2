import bgu.spl.mics.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;



public class FutureTest {
    private Future<Integer> future;
    private Field currentResult;

    @Before
    public void setUp() throws ClassNotFoundException, NoSuchFieldException {
        this.future = createFuture();
        this.currentResult =Class.forName("bgu.spl.mics.Future").getDeclaredField("result");
        currentResult.setAccessible(true);
    }


    protected Future<Integer> createFuture() {
        return new Future<>();
    }
    /**
     *
     * Test Query Method for {@link Future#get()}
     */
    @Test
    public void get() {
        Integer i2=4;
        future.resolve(i2);
        Integer result =future.get();
        Assert.assertEquals(i2,result);
    }

    /**
     * Test Command Method for {@link Future#resolve(Object)}
     */
    @Test
    public void resolve() {
        Assert.assertFalse(future.isDone());
        Integer i3= 9;
        future.resolve(i3);
        assertTrue(future.isDone());
        assertEquals(i3,future.get());
    }

    /**
     * Test Query Method for {@link Future#isDone()}
     */
    @Test
    public void isDone() {
        Integer i1=7;
        Assert.assertFalse(future.isDone());
        future.resolve(i1);
        Assert.assertTrue(future.isDone());
    }

    /**
     * Test Query Method for {@link Future#get(long, TimeUnit) positive check we expect to get the result}
     */
    @Test
    public void getTimed()  {
        Integer i4=8;
        Thread t2=new Thread(()->{
            try {
                Thread.sleep(950);
            }
            catch (InterruptedException e){
                fail(e.getMessage());
            }
            future.resolve(i4);
        });
        t2.start();
        Integer result= future.get(2,TimeUnit.SECONDS);
        Assert.assertEquals(i4,result);
        Integer result2= null ;
        try {
            result2 =(Integer) currentResult.get(future);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(i4,result2);
    }


    /**
     * Test Query Method for {@link Future#get(long, TimeUnit)
     * negative check we expect to get the result==null}
     */
    @Test
    public void catchNull()  {
        Integer i6=6;
        Thread t2=new Thread(()->{
            try {
                Thread.sleep(2050);
            }
            catch (InterruptedException e){
                fail(e.getMessage());
            }
            future.resolve(i6);
        });
        Thread t1=new Thread(()->future.get(2,TimeUnit.SECONDS));
        t1.start();
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer result2= null;
        try {
            result2 = (Integer) currentResult.get(future);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ;
        Assert.assertNull(result2);
    }
}