package bgu.spl.mics.application.parsing.ThirdPart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class time {

    @SerializedName("speed")
    @Expose
    private int speed;

    @SerializedName("duration")
    @Expose
    private int duration;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
