package fptu.ninhtbm.thebookshop.models;

import com.google.firebase.Timestamp;

public class Stock {
    private String id;
    private int quantity;
    private Timestamp updatedDate;

    public Stock() {
    }

    public Stock(String id, int quantity, Timestamp updatedDate) {
        this.id = id;
        this.quantity = quantity;
        this.updatedDate = updatedDate;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    };

    public int getQuantity() {
        return quantity;
    };

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    };

    public Timestamp getUpdatedDate() {
        return updatedDate;
    };

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    };

    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", quantity=" + quantity +
                ", updatedDate=" + updatedDate +
                '}';
    }
}