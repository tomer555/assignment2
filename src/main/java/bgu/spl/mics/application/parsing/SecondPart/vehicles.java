package bgu.spl.mics.application.parsing.SecondPart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class vehicles {

    @SerializedName("license")
    @Expose
    private int license;

    @SerializedName("speed")
    @Expose
    private int speed;

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
