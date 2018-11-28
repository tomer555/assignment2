package bgu.spl.mics.application.parsing.FirstPart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class initialInventory {

    @SerializedName("bookTitle")
    @Expose
    private String bookTitle;

    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("price")
    @Expose
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
