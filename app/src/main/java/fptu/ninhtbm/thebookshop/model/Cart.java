package fptu.ninhtbm.thebookshop.model;

import com.google.firebase.Timestamp;

public class Cart {
    private String id;
    private Object customerID;
    private Timestamp createdAt;

    public Cart() {
    }

    public Cart(String id, Object customerID, Timestamp createdAt) {
        this.id = id;
        this.customerID = customerID;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    };

    public Object getCustomerID() {
        return customerID;
    };

    public void setCustomerID(Object customerID) {
        this.customerID = customerID;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", customerID=" + customerID +
                ", createdAt=" + createdAt +
                '}';
    }
}