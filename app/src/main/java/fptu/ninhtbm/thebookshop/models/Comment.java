package fptu.ninhtbm.thebookshop.models;

import com.google.firebase.Timestamp;

public class Comment {
    private String id;
    private Object bookID;
    private Object customerID;
    private int rate;
    private Timestamp createdAt;

    public Comment() {
    }

    public Comment(String id, Object bookID, Object customerID, int rate, Timestamp createdAt) {
        this.id = id;
        this.bookID = bookID;
        this.customerID = customerID;
        this.rate = rate;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    };

    public Object getBookID() {
        return bookID;
    };

    public void setBookID(Object bookID) {
        this.bookID = bookID;
    };

    public Object getCustomerID() {
        return customerID;
    };

    public void setCustomerID(Object customerID) {
        this.customerID = customerID;
    };

    public int getRate() {
        return rate;
    };

    public void setRate(int rate) {
        this.rate = rate;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", bookID=" + bookID +
                ", customerID=" + customerID +
                ", rate=" + rate +
                ", createdAt=" + createdAt +
                '}';
    }
}