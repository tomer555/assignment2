package bgu.spl.mics.application;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.parsing.FirstPart.initialInventory;
import bgu.spl.mics.application.parsing.JsonReader;
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
    private static int orderId=0;

    public static void main(String[] args) throws FileNotFoundException {
        //--------------Connection to json input file --------------------
        File file =new File("input.json");
        FileReader fileReader=new FileReader(file);
        Gson gson=new Gson();
        JsonReader reader= gson.fromJson(fileReader,JsonReader.class);

        //Creating Singleton Classes
        Inventory library=Inventory.getInstance();
        ResourcesHolder resources =ResourcesHolder.getInstance();
        MoneyRegister moneyRegister=MoneyRegister.getInstance();

        //Parsing Books
        addBooksToInventory(reader,library);

        //Parsing DeliveryVehicles
        addVehiclesToResource(reader,resources);

        //Parsing Services
        services services=reader.getServices();

        //Parsed Customers+Api Services
        ParseCustomerAndApi(reader,services);


        //--------Parsing Service Amounts---------------
        int sellersAmount =services.getSelling();
        int logisticAmount= services.getLogistics();
        int inventoryAmount =services.getInventoryService();
        int resourceAmount= services.getResourcesService();


        //--------------Creating Sellers--------------------
        for(int i=1;i<=sellersAmount;i++){
            SellingService seller=new SellingService("seller "+i,moneyRegister);
            Thread Tseller=new Thread(seller);
            Tseller.start();

        }

        //------------Creating Logistics------------
        for(int i=1;i<=logisticAmount;i++){
            LogisticsService logistic=new LogisticsService("logistic "+i);
            Thread Tlogistic=new Thread(logistic);
            Tlogistic.start();
        }

        //------------Creating InventoryServices------------
        for(int i=1;i<=inventoryAmount;i++){
            InventoryService inventoryService=new InventoryService("inventoryService "+i,library);
            Thread Tinventory=new Thread(inventoryService);
            Tinventory.start();
        }

        //------------Creating ResourceServices------------
        for(int i=1;i<=resourceAmount;i++){
            ResourceService service=new ResourceService("logistic "+i,resources);
            Thread Tresource=new Thread(service);
            Tresource.start();
        }

        //-----Parsing Time-------
        time parsedTime=services.getTime();

        //Creating Singleton TimeService
        TimeService globalTimer=new TimeService("Global Timer",parsedTime.getSpeed(),parsedTime.getDuration());
        Thread timeThread=new Thread(globalTimer);
        timeThread.start();


        System.exit(0);

    }

    private static void ParseCustomerAndApi(JsonReader reader, services services){
        List<customers> parsedCustomers= services.getCustomers();
        List<Customer> customers= new LinkedList<>();
        parsedCustomers.forEach((c)->{
            List<orderSchedule> parsedSchdules= c.getOrderSchedules();
            creditCard card=c.getCreditCard();
            Customer customer=new Customer(c.getName(),c.getId(),c.getAddress(),c.getDistance(),card.getNumber(),card.getAmount());
            customers.add(customer);
            List<OrderReceipt> orderReceipts=new LinkedList<>();
            parsedSchdules.forEach((p)->{
                OrderReceipt receipt =new OrderReceipt(orderId++,c.getId(),p.getBookTitle(),p.getTick());
                orderReceipts.add(receipt);
            });
            APIService apiService=new APIService("API: "+customer.getName(),orderReceipts,customer);
            Thread Tapi=new Thread(apiService);
            Tapi.start();
        });
    }


    private static void addVehiclesToResource(JsonReader reader, ResourcesHolder resources){
        List<bgu.spl.mics.application.parsing.SecondPart.vehicles> parsedCars=reader.getCars();
        DeliveryVehicle[] array=new DeliveryVehicle[parsedCars.size()];
        for(int i=0;i<array.length;i++){
            bgu.spl.mics.application.parsing.SecondPart.vehicles d =parsedCars.get(i);
            array[i]=new DeliveryVehicle(d.getLicense(),d.getSpeed());
        }
        if(resources!=null){ ;
            resources.load(array);
        }
    }



    private  static void addBooksToInventory(JsonReader reader,Inventory inventory){
        List<initialInventory> parsedBooks = reader.getBooks();
        BookInventoryInfo[] books= new BookInventoryInfo[parsedBooks.size()];
        for(int i=0;i<books.length;i++){
            initialInventory b=parsedBooks.get(i);
            books[i]=new BookInventoryInfo(b.getBookTitle(),b.getAmount(),b.getPrice());
        }

        if (inventory != null) {
            inventory.load(books);
        }

    }
}
