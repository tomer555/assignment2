package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

public class Deserialize {

    @SuppressWarnings("unchecked")
    public static HashMap<String,Integer> deserializeInv(String path){
        HashMap<String,Integer> output=null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            BufferedInputStream bf=new BufferedInputStream(file);
            ObjectInputStream in = new ObjectInputStream(bf);
            // Method for deserialization of object
            output =(HashMap<String,Integer>)  in.readObject();
            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }



    @SuppressWarnings("unchecked")
    public static HashMap<Integer, Customer> deserializeCustomers(String path){
        HashMap<Integer,Customer> output=null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            BufferedInputStream bf=new BufferedInputStream(file);
            ObjectInputStream in = new ObjectInputStream(bf);
            // Method for deserialization of object
            output =(HashMap<Integer,Customer>)  in.readObject();
            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }


    @SuppressWarnings("unchecked")
    public static List<OrderReceipt> deserializeOrders(String path){
        List<OrderReceipt> output=null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            BufferedInputStream bf=new BufferedInputStream(file);
            ObjectInputStream in = new ObjectInputStream(bf);
            // Method for deserialization of object
            output =(List<OrderReceipt>)  in.readObject();
            in.close();
            file.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }


    @SuppressWarnings("unchecked")
    public static MoneyRegister deserializeMoneyRegister(String path){
        MoneyRegister output=null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            BufferedInputStream bf=new BufferedInputStream(file);
            ObjectInputStream in = new ObjectInputStream(bf);
            // Method for deserialization of object
            output =(MoneyRegister)  in.readObject();
            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }



}
