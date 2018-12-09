package bgu.spl.mics.application;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


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
