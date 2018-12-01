package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Customer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Serialize {

    public static void serializeObject(String path, Object object){
        try{
            FileOutputStream fileOut=new FileOutputStream(path);
            ObjectOutputStream out =new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
