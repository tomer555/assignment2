package bgu.spl.mics.application.parsing.ThirdPart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class orderSchedule {

    @SerializedName("bookTitle")
    @Expose
    private String bookTitle;

    @SerializedName("tick")
    @Expose
    private int tick;

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
