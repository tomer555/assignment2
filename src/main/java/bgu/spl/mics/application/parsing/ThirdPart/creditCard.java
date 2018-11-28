package bgu.spl.mics.application.parsing.ThirdPart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class creditCard {
    @SerializedName("number")
    @Expose
    private int number;

    @SerializedName("amount")
    @Expose
    private int amount;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
