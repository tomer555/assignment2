package bgu.spl.mics.application;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.parsing.FirstPart.initialInventory;
import bgu.spl.mics.application.parsing.JsonReader;
import bgu.spl.mics.application.parsing.SecondPart.initialResources;
import bgu.spl.mics.application.parsing.SecondPart.vehicles;
import bgu.spl.mics.application.parsing.ThirdPart.*;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    private static int orderId = 0;
    private static final Object lockMain=new Object();

    public static void main(String[] args) throws FileNotFoundException {
        //--------------Connection to json input file --------------------
        File file = new File(args[0]);
        FileReader fileReader = new FileReader(file);
        Gson gson = new Gson();
        JsonReader reader = gson.fromJson(fileReader, JsonReader.class);
        List<Thread> threadList=new LinkedList<>();

        //------------services lists-----------------------


        //Creating Singleton Classes
        Inventory library = Inventory.getInstance();
        ResourcesHolder resources = ResourcesHolder.getInstance();
        MoneyRegister moneyRegister = MoneyRegister.getInstance();

        //Parsing Books
        addBooksToInventory(reader, library);

        //Parsing DeliveryVehicles
        addVehiclesToResource(reader, resources);

        //Parsing Services
        services services = reader.getServices();

        //Parsed Customers+Api Services
        ParseCustomerAndApi(services,threadList);


        //--------Parsing Service Amounts---------------
        int sellersAmount = services.getSelling();
        int logisticAmount = services.getLogistics();
        int inventoryAmount = services.getInventoryService();
        int resourceAmount = services.getResourcesService();


        //--------------Creating Sellers--------------------
        for (int i = 1; i <= sellersAmount; i++) {
            SellingService seller = new SellingService("seller " + i, moneyRegister);
            Thread Tseller = new Thread(seller);
            threadList.add(Tseller);
            Tseller.start();

        }

        //------------Creating Logistics------------
        for (int i = 1; i <= logisticAmount; i++) {
            LogisticsService logistic = new LogisticsService("logistic " + i);
            Thread Tlogistic = new Thread(logistic);
            threadList.add(Tlogistic);
            Tlogistic.start();
        }

        //------------Creating InventoryServices------------
        for (int i = 1; i <= inventoryAmount; i++) {
            InventoryService inventoryService = new InventoryService("inventoryService " + i, library);
            Thread Tinventory = new Thread(inventoryService);
            threadList.add(Tinventory);
            Tinventory.start();
        }

        //------------Creating ResourceServices------------
        for (int i = 1; i <= resourceAmount; i++) {
            ResourceService service = new ResourceService("resource " + i, resources);
            Thread Tresource = new Thread(service);
            threadList.add(Tresource);
            Tresource.start();
        }


        //-----Parsing Time-------
        time parsedTime = services.getTime();


        //Creating Singleton TimeService
        TimeService globalTimer = new TimeService("Global Timer",lockMain, parsedTime.getSpeed(), parsedTime.getDuration());
        Thread timeThread = new Thread(globalTimer);
        timeThread.start();

        synchronized (lockMain) {
            try {
                lockMain.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        threadList.forEach(Thread::interrupt);
        //Prints Program output into files
        //print to file all customers in hashmap args[1]
        library.printInventoryToFile(args[2]);
        moneyRegister.printOrderReceipts(args[3]);
        //prints Money register object args[4]


        System.exit(0);
        }



    private static void ParseCustomerAndApi(services services, List<Thread> threadList) {
        List<customers> parsedCustomers = services.getCustomers();
        List<Customer> customers = new LinkedList<>();
        parsedCustomers.forEach((c) -> {
            List<orderSchedule> parsedSchdules = c.getOrderSchedules();
            creditCard card = c.getCreditCard();
            Customer customer = new Customer(c.getName(), c.getId(), c.getAddress(), c.getDistance(), card.getNumber(), card.getAmount());
            customers.add(customer);
            List<OrderReceipt> orderReceipts = new LinkedList<>();
            parsedSchdules.forEach((p) -> {
                OrderReceipt receipt = new OrderReceipt(orderId++, c.getId(), p.getBookTitle(), p.getTick());
                orderReceipts.add(receipt);
            });
            APIService apiService = new APIService("API: " + customer.getName(), orderReceipts, customer);
            Thread Tapi = new Thread(apiService);
            threadList.add(Tapi);
            Tapi.start();
        });
    }


    private static void addVehiclesToResource(JsonReader reader, ResourcesHolder resources) {
        List<initialResources> parsedCars = reader.getVehicles();
        initialResources v = parsedCars.get(0);
        List<vehicles> cars = v.getVehicles();
        DeliveryVehicle[] array = new DeliveryVehicle[cars.size()];
        for (vehicles d : cars) {
            for (int i = 0; i < array.length; i++) {
                array[i] = new DeliveryVehicle(d.getLicense(), d.getSpeed());
            }
        }
        if (resources != null) {
            resources.load(array);
        }

    }


    private static void addBooksToInventory(JsonReader reader, Inventory inventory) {
        List<initialInventory> parsedBooks = reader.getBooks();
        BookInventoryInfo[] books = new BookInventoryInfo[parsedBooks.size()];
        for (int i = 0; i < books.length; i++) {
            initialInventory b = parsedBooks.get(i);
            books[i] = new BookInventoryInfo(b.getBookTitle(), b.getAmount(), b.getPrice());
        }

        if (inventory != null) {
            inventory.load(books);
        }
    }
}

