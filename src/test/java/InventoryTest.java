import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
public class InventoryTest {

    private List<BookInventoryInfo> listOfBooks;
    private  Inventory instance;
    private BookInventoryInfo book1;
    private BookInventoryInfo book2;
    private BookInventoryInfo book3;
    private BookInventoryInfo[ ] inventory;
    private Field booksList;
    private List<BookInventoryInfo> toCheck;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws ClassNotFoundException, NoSuchFieldException {
            this.listOfBooks = new Vector<>();
            this.book1=new BookInventoryInfo("name1",2,60);
            this.book2=new BookInventoryInfo("name3",4,80);
            this.book3=new BookInventoryInfo("name3",0,80);
            this.inventory=new BookInventoryInfo[]{book1,book2,book3};
            instance=Inventory.getInstance();
            this.booksList=Class.forName("bgu.spl.mics.application.passiveObjects.Inventory").getDeclaredField("listOfBooks");
            booksList.setAccessible(true);
        try {
            this.toCheck= (List<BookInventoryInfo>) booksList.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        this.listOfBooks=null;
        this.book1=null;
        this.book2=null;
        this.book3=null;
        this.inventory=null;
        try {
            booksList.set(instance,new LinkedList<BookInventoryInfo>());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.instance=null;
        booksList.setAccessible(false);

        this.booksList=null;
        this.toCheck=null;
    }



    @Test
    public void getInstance() {
        Assert.assertNotNull(Inventory.getInstance());
    }

    @Test

    public void load () {
        instance.load(inventory);
        BookInventoryInfo []books=new BookInventoryInfo[toCheck.size()];
        for (int i=0;i<toCheck.size();i++) {
            books[i]=toCheck.get(i);
        }
        Assert.assertArrayEquals(inventory,books);
    }


    @Test
    public void take () {
        instance.load(inventory);
        instance.take(book1.getBookTitle());
        Assert.assertEquals(1,book1.getAmountInInventory());

    }

    @Test
    public void checkAvailabilityAndGetPrice() {
        instance.load(inventory);
        if (listOfBooks.contains(book1)) {
            Assert.assertEquals((instance.checkAvailabilityAndGetPrice(book1.getBookTitle())),60);
        }

    }
    @Test
    public void checkAvailabilityAndGetPrice2() {
        instance.load(inventory);
        if (listOfBooks.contains(book1)) {
            Assert.assertEquals((instance.checkAvailabilityAndGetPrice(book1.getBookTitle())),-1);
        }

    }


}
