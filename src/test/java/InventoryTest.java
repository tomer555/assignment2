

import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collections;
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

    @Before
    public void setUp() throws ClassNotFoundException, NoSuchFieldException {
            this.listOfBooks = new Vector<>();
            this.book1=new BookInventoryInfo("name1",2,60);
            this.book2=new BookInventoryInfo("name3",4,80);
            this.book3=new BookInventoryInfo("name3",0,80);
            this.inventory=new BookInventoryInfo[]{book1,book2,book3};
            instance=Inventory.getInstance();
            this.booksList=Class.forName("bgu.spl.mics.application.passiveObjects.Inventory").getDeclaredField("listOfBooks");
            booksList.setAccessible(true);
    }


    @Test
    public void getInstance() {
        Assert.assertNotNull(Inventory.getInstance());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void load () {
        instance.load(inventory);
        try {
            List<BookInventoryInfo> toCheck= (List<BookInventoryInfo>) booksList.get(instance);
            BookInventoryInfo []books=new BookInventoryInfo[toCheck.size()];
            for (int i=0;i<toCheck.size();i++) {
                books[i]=toCheck.get(i);
            }

            Assert.assertArrayEquals(books,inventory);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void take () {
        //we should take book1
        instance.load(inventory);
        instance.take(book1.getBookTitle());
        Assert.assertEquals(1,book1.getAmountInInventory());

    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        instance.load(inventory);
        if (listOfBooks.contains(book1)) {
            Assert.assertEquals((instance.checkAvailabilityAndGetPrice(book1.getBookTitle())),60);
        }

    }
    @Test
    public void checkAvailabiltyAndGetPrice2() {
        instance.load(inventory);
        if (listOfBooks.contains(book1)) {
            Assert.assertEquals((instance.checkAvailabilityAndGetPrice(book1.getBookTitle())),-1);
        }

    }

    @Test
    public void printInventoryToFile() {
     // Assert.assertEquals(instance.printInventoryToFile("name4"),"kk");

    }
}
/*
  Collections.addAll(this.listOfBooks,inventory);
        for (BookInventoryInfo book:listOfBooks) {
            for (int i=0;i<inventory.length;i++) {
                if (!book.getBookTitle().equals(inventory[i].getBookTitle()))
                    System.out.print("not good");
            }
        }
        for (int i=0;i<inventory.length;i++) {
            if (!listOfBooks.contains(inventory[i]))
                System.out.print("not good");
        }
        */