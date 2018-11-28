package bgu.spl.mics.application.parsing;
import bgu.spl.mics.application.parsing.FirstPart.initialInventory;
import bgu.spl.mics.application.parsing.SecondPart.initialResources;
import bgu.spl.mics.application.parsing.SecondPart.vehicles;
import bgu.spl.mics.application.parsing.ThirdPart.services;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JsonReader {
        @SerializedName("initialInventory")
        @Expose
        private List<initialInventory> books = new ArrayList<>();

        @SerializedName("initialResources")
        @Expose
        private List<initialResources> vehicles = new ArrayList<>();

        @SerializedName("vehicles")
        @Expose
        private List<vehicles> cars  = new ArrayList<>();

        @SerializedName("services")
        @Expose
        private services services;


    public bgu.spl.mics.application.parsing.ThirdPart.services getServices() {
        return services;
    }

    public void setServices(bgu.spl.mics.application.parsing.ThirdPart.services services) {
        this.services = services;
    }

    public List<bgu.spl.mics.application.parsing.SecondPart.vehicles> getCars() {
        return cars;
    }

    public void setCars(List<bgu.spl.mics.application.parsing.SecondPart.vehicles> cars) {
        this.cars = cars;
    }

    public List<initialResources> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<initialResources> vehicles) {
        this.vehicles = vehicles;
    }

    public List<initialInventory> getBooks() {
        return books;
    }

    public void setBooks(List<initialInventory> books) {
        this.books = books;
    }
}

