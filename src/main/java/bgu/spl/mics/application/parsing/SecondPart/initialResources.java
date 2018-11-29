package bgu.spl.mics.application.parsing.SecondPart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class initialResources {

    @SerializedName("vehicles")
    @Expose
    private List<vehicles> vehicles=new ArrayList<>();


    public List<bgu.spl.mics.application.parsing.SecondPart.vehicles> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<bgu.spl.mics.application.parsing.SecondPart.vehicles> vehicles) {
        this.vehicles = vehicles;
    }
}
