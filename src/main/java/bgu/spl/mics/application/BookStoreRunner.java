package bgu.spl.mics.application;
import bgu.spl.mics.application.parsing.FirstPart.initialInventory;
import bgu.spl.mics.application.parsing.JsonReader;
import bgu.spl.mics.application.parsing.ThirdPart.creditCard;
import bgu.spl.mics.application.parsing.ThirdPart.customers;
import bgu.spl.mics.application.parsing.ThirdPart.services;
import bgu.spl.mics.application.parsing.ThirdPart.time;
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
    private int orderId=0;
    public static void main(String[] args) throws FileNotFoundException {
        //--------------Connection to json input file --------------------
        File file =new File("input.json");
        FileReader fileReader=new FileReader(file);
        Gson gson=new Gson();
        JsonReader reader= gson.fromJson(fileReader,JsonReader.class);

        //Creating Inventory for books
        Inventory library=Inventory.getInstance();

        //-------------Parsing Books-------------------------------
        List<initialInventory> parsedBooks = reader.getBooks();
        List<BookInventoryInfo> books= new LinkedList<>();
        parsedBooks.forEach((b)->books.add(new BookInventoryInfo(b.getBookTitle(),b.getAmount(),b.getPrice())));
        if (library != null) {
            library.load((BookInventoryInfo[])books.toArray());
        }

        //Creating ResourceHolder for DeliveryVehicles
        ResourcesHolder resources =ResourcesHolder.getInstance();

        //-------------Parsing DeliveryVehicles-------------------------------
        List<bgu.spl.mics.application.parsing.SecondPart.vehicles> parsedCars=reader.getCars();
        List<DeliveryVehicle> cars= new LinkedList<>();
        parsedCars.forEach((c)->cars.add(new DeliveryVehicle(c.getLicense(),c.getSpeed())));
        if(resources!=null){
            resources.load((DeliveryVehicle[])cars.toArray());
        }

        //Creating MoneyRegister for OrderReceipt
        MoneyRegister moneyRegister=MoneyRegister.getInstance();


        //----------------------------Parsing Services-------------------------
        services services=reader.getServices();

        //-----Parsing Time-------
        time parsedTime=services.getTime();

        //Creating TimeService
        TimeService globalTimer=new TimeService("Global Timer",parsedTime.getSpeed(),parsedTime.getDuration());

        //--------Parsing Service Amounts---------------
        int sellersAmount =services.getSelling();
        int logisticAmount= services.getLogistics();
        int inventoryAmount =services.getInventoryService();
        int resourceAmount= services.getResourcesService();

        //--------------Creating Sellers--------------------
        List<SellingService> sellers=new LinkedList<>();
        for(int i=1;i<=sellersAmount;i++){
            sellers.add(new SellingService("seller "+i,moneyRegister));
        }

        //------------Creating Logistics------------
        List<LogisticsService> logistics=new LinkedList<>();
        for(int i=1;i<=logisticAmount;i++){
            logistics.add(new LogisticsService("logistic "+i));
        }

        //------------Creating InventoryServices------------
        List<InventoryService> inventoryServices=new LinkedList<>();
        for(int i=1;i<=inventoryAmount;i++){
            inventoryServices.add(new InventoryService("inventoryService "+i,library));
        }

        //------------Creating ResourceServices------------
        List<ResourceService> resourceServices=new LinkedList<>();
        for(int i=1;i<=resourceAmount;i++){
            resourceServices.add(new ResourceService("logistic "+i,resources));
        }

        //------------Parsed Customers+Api Services-----------------------
        List<APIService> apiServices =new LinkedList<>();

        List<customers> parsedCustomers= services.getCustomers();
        List<Customer> customers= new LinkedList<>();
        parsedCustomers.forEach((c)->{
            creditCard card=c.getCreditCard();
            customers.add(new Customer(c.getName(),c.getId(),c.getAddress(),c.getDistance(),card.getNumber(),card.getAmount()));
        });



        //----------------



    }
}
