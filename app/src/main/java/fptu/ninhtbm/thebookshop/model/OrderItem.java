package fptu.ninhtbm.thebookshop.model;

public class OrderItem {
    private String id;
    private Object bookID;
    private Object orderID;
    private int quantity;
    private double sellPrice;
    private double discount;

    public OrderItem() {
    }

    public OrderItem(Object bookID, Object orderID, int quantity, double sellPrice, double discount) {
        this.bookID = bookID;
        this.orderID = orderID;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
        this.discount = discount;
    }

    public OrderItem(String id, Object bookID, Object orderID, int quantity, double sellPrice, double discount) {
        this.id = id;
        this.bookID = bookID;
        this.orderID = orderID;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
        this.discount = discount;
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

    public Object getOrderID() {
        return orderID;
    };

    public void setOrderID(Object orderID) {
        this.orderID = orderID;
    };

    public int getQuantity() {
        return quantity;
    };

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    };

    public double getSellPrice() {
        return sellPrice;
    };

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    };

    public double getDiscount() {
        return discount;
    };

    public void setDiscount(double discount) {
        this.discount = discount;
    };

    @Override
    public String toString() {
        return "OrderItem{" +
                "id='" + id + '\'' +
                ", bookID=" + bookID +
                ", orderID=" + orderID +
                ", quantity=" + quantity +
                ", sellPrice=" + sellPrice +
                ", discount=" + discount +
                '}';
    }
}