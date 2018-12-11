package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramOut {
    public static void main(String[] args){
        //Deserialize
        MoneyRegister outputMoney = Deserialize.deserializeMoneyRegister(args[4]);
        List<OrderReceipt> outputOrders=Deserialize.deserializeOrders(args[3]);
        HashMap<String,Integer> outputInv = Deserialize.deserializeInv(args[2]);
        HashMap<Integer,Customer> outputCustomers=Deserialize.deserializeCustomers(args[1]);

        //Printing
        System.out.println("Customers:");
        for (Map.Entry<Integer, Customer> entry : outputCustomers.entrySet()) {
            Integer key = entry.getKey();
            Customer value = entry.getValue();
            System.out.println(key);
            System.out.println(value);
        }

        System.out.println("Books:");
        for (Map.Entry<String, Integer> entry : outputInv.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Name: "+key+" Quantity: "+value);
        }
        System.out.println();

        System.out.println("Receipts:");
        outputOrders.forEach(System.out::println);
        System.out.println("Store Earnings:");
        System.out.println(outputMoney.getTotalEarnings());

    }

}
