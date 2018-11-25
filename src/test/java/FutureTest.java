import bgu.spl.mics.Future;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;



public class FutureTest {
    private Future<Integer> future;

    @Before
    public void setUp(){
        this.future = createFuture();
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
        Integer result1=future.get(1,TimeUnit.SECONDS);
        Assert.assertNull(result1);
        Thread t2=new Thread(()->{
            try {
                Thread.sleep(950);
            }
            catch (InterruptedException e){
                fail(e.getMessage());
            }
            future.resolve(i4);
        });
        Thread t1=new Thread(()->future.get(1,TimeUnit.SECONDS));
        t1.start();
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
        Integer result2=future.getResult();
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
        Integer result2=future.getResult();
        Assert.assertNull(result2);
    }




    /**
     *Test Query Method for {@link Future#getResult()}  For test purposes only!
     */
    @Test
    public void getResult() {
        Assert.assertNull(future.getResult());
        Integer i5 =5;
        future.resolve(i5);
        Assert.assertEquals(i5,future.getResult());
    }
}