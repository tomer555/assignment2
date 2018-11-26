

import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

public class InventoryTest {
    private List<BookInventoryInfo> listOfBooks;
    private Inventory instance;

    @Before
    public void setUp() throws ClassNotFoundException, NoSuchFieldException {
        this.listOfBooks = createListOfBooks();
        instance=Inventory.getInstance();

    }


    protected List<BookInventoryInfo> createListOfBooks() {
        return new Vector<BookInventoryInfo>();
    }

    /**
     *
     * Test Query Method for {@link Future#get()}
     */

    @Test
    public void getInstance() {
    }

    @Test
    public void load() {
    }

    @Test
    public void take() {
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
    }

    @Test
    public void printInventoryToFile() {
    }
}