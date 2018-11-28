package bgu.spl.mics.application.parsing.ThirdPart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class customers {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("creditCard")
    @Expose
    private creditCard creditCard;

    @SerializedName("orderSchedule")
    @Expose
    private List<orderSchedule> orderSchedules;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<orderSchedule> getOrderSchedules() {
        return orderSchedules;
    }

    public void setOrderSchedules(List<orderSchedule> orderSchedules) {
        this.orderSchedules = orderSchedules;
    }

    public bgu.spl.mics.application.parsing.ThirdPart.creditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(bgu.spl.mics.application.parsing.ThirdPart.creditCard creditCard) {
        this.creditCard = creditCard;
    }
}
