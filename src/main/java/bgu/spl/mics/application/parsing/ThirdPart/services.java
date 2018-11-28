package bgu.spl.mics.application.parsing.ThirdPart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class services {

    @SerializedName("selling")
    @Expose
    private int selling;

    @SerializedName("inventoryService")
    @Expose
    private int inventoryService;

    @SerializedName("logistics")
    @Expose
    private int logistics;

    @SerializedName("resourcesService")
    @Expose
    private int resourcesService;

    @SerializedName("time")
    @Expose
    private time time;

    @SerializedName("customers")
    @Expose
    private List<customers> customers;

    public int getSelling() {
        return selling;
    }

    public void setSelling(int selling) {
        this.selling = selling;
    }

    public int getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(int inventoryService) {
        this.inventoryService = inventoryService;
    }

    public int getLogistics() {
        return logistics;
    }

    public void setLogistics(int logistics) {
        this.logistics = logistics;
    }

    public int getResourcesService() {
        return resourcesService;
    }

    public void setResourcesService(int resourcesService) {
        this.resourcesService = resourcesService;
    }


    public bgu.spl.mics.application.parsing.ThirdPart.time getTime() {
        return time;
    }

    public void setTime(bgu.spl.mics.application.parsing.ThirdPart.time time) {
        this.time = time;
    }

    public List<bgu.spl.mics.application.parsing.ThirdPart.customers> getCustomers() {
        return customers;
    }

    public void setCustomers(List<bgu.spl.mics.application.parsing.ThirdPart.customers> customers) {
        this.customers = customers;
    }
}
