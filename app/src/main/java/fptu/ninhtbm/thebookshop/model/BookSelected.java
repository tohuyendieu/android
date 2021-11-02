package fptu.ninhtbm.thebookshop.model;

import com.google.firebase.Timestamp;

public class BookSelected {
    private String id;
    private Object bookID;
    private Object cartID;
    private int quantity;
    private Timestamp createdAt;

    public BookSelected() {
    }

    public BookSelected(String id, Object bookID, Object cartID, int quantity, Timestamp createdAt) {
        this.id = id;
        this.bookID = bookID;
        this.cartID = cartID;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public BookSelected(Object bookID, Object cartID, int quantity, Timestamp createdAt) {
        this.bookID = bookID;
        this.cartID = cartID;
        this.quantity = quantity;
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

    public Object getCartID() {
        return cartID;
    };

    public void setCartID(Object cartID) {
        this.cartID = cartID;
    };

    public int getQuantity() {
        return quantity;
    };

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

    @Override
    public String toString() {
        return "BookSelected{" +
                "id='" + id + '\'' +
                ", bookID=" + bookID +
                ", cartID=" + cartID +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }
}