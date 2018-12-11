package bgu.spl.mics.application;
import bgu.spl.mics.application.parsing.FirstPart.initialInventory;
import bgu.spl.mics.application.parsing.JsonReader;
import bgu.spl.mics.application.parsing.SecondPart.initialResources;
import bgu.spl.mics.application.parsing.SecondPart.vehicles;
import bgu.spl.mics.application.parsing.ThirdPart.*;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    private static int orderId = 0;
    public static void main(String[] args) throws FileNotFoundException {
        //--------------Connection to json input file --------------------
        File file = new File(args[0]);
        FileReader fileReader = new FileReader(file);
        Gson gson = new Gson();
        JsonReader reader = gson.fromJson(fileReader, JsonReader.class);

        HashMap<Integer,Customer> customers=new HashMap<>();


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


        //--------Parsing Service Amounts---------------
        int sellersAmount = services.getSelling();
        int logisticAmount = services.getLogistics();
        int inventoryAmount = services.getInventoryService();
        int resourceAmount = services.getResourcesService();
        int apiAmount=services.getCustomers().size();

        //the amount of services except the Time Service
        int ServicesCount=sellersAmount+logisticAmount+inventoryAmount+resourceAmount+apiAmount;

        // countDownLatch for timer to start when all services finished subscribing
        CountDownLatch startSignal = new CountDownLatch(ServicesCount);

        // countDownLatch for Main to know that all services got Terminated
        CountDownLatch endSignal = new CountDownLatch(ServicesCount+1);

        //Parsed Customers+Api Services
        ParseCustomerAndApi(services,customers,startSignal,endSignal);

        //--------------Creating Sellers--------------------
        for (int i = 1; i <= sellersAmount; i++) {
            SellingService seller = new SellingService("seller " + i, moneyRegister,startSignal,endSignal);
            Thread Tseller = new Thread(seller);
            Tseller.start();

        }

        //------------Creating Logistics------------
        for (int i = 1; i <= logisticAmount; i++) {
            LogisticsService logistic = new LogisticsService("logistic " + i,startSignal,endSignal);
            Thread Tlogistic = new Thread(logistic);
            Tlogistic.start();
        }

        //------------Creating InventoryServices------------
        for (int i = 1; i <= inventoryAmount; i++) {
            InventoryService inventoryService = new InventoryService("inventoryService " + i, library,startSignal,endSignal);
            Thread Tinventory = new Thread(inventoryService);
            Tinventory.start();
        }

        //------------Creating ResourceServices------------
        for (int i = 1; i <= resourceAmount; i++) {
            ResourceService service = new ResourceService("resource " + i, resources,startSignal,endSignal);
            Thread Tresource = new Thread(service);
            Tresource.start();
        }


        //-----Parsing Time-------
        time parsedTime = services.getTime();


        //Creating Singleton TimeService
        TimeService globalTimer = new TimeService("Global Timer", parsedTime.getSpeed(), parsedTime.getDuration(),startSignal,endSignal);
        Thread timeThread = new Thread(globalTimer);
        timeThread.start();


        try {
            endSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //Prints Program output into files
        Serialize.serializeObject(args[1],customers);
        library.printInventoryToFile(args[2]);
        moneyRegister.printOrderReceipts(args[3]);
        Serialize.serializeObject(args[4],moneyRegister);


        System.exit(0);
    }






        private static void ParseCustomerAndApi(services services,HashMap<Integer,Customer> customers,CountDownLatch startSignal,CountDownLatch endSignal) {
        List<customers> parsedCustomers = services.getCustomers();
        parsedCustomers.forEach((c) -> {
            List<orderSchedule> parsedSchdules = c.getOrderSchedules();
            creditCard card = c.getCreditCard();
            Customer customer = new Customer(c.getName(), c.getId(), c.getAddress(), c.getDistance(), card.getNumber(), card.getAmount());
            List<OrderReceipt> orderReceipts = new LinkedList<>();
            parsedSchdules.forEach((p) -> {
                OrderReceipt receipt = new OrderReceipt(orderId++, c.getId(), p.getBookTitle(), p.getTick());
                orderReceipts.add(receipt);
            });
            customers.put(customer.getId(),customer);
            APIService apiService = new APIService("API: " + customer.getName(), orderReceipts, customer,startSignal,endSignal);
            Thread Tapi = new Thread(apiService);
            Tapi.start();
        });
    }


    private static void addVehiclesToResource(JsonReader reader, ResourcesHolder resources) {
        List<initialResources> parsedCars = reader.getVehicles();
        initialResources v = parsedCars.get(0);
        List<vehicles> cars = v.getVehicles();
        DeliveryVehicle[] array = new DeliveryVehicle[cars.size()];
        int i=0;
        for (vehicles d : cars)
            array[i++] = new DeliveryVehicle(d.getLicense(), d.getSpeed());
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

