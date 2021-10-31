package fptu.ninhtbm.thebookshop.ui.models;

import com.google.firebase.Timestamp;

public class Order {
    private String id;
    private Object customerID;
    private Object orderStatusID;
    private Timestamp orderDate;
    private double totalAmount;
    private String buyerFullname;
    private String buyerPhone;
    private String buyerAddress;

    public Order() {
    }

    public Order(String id, Object customerID, Object orderStatusID, Timestamp orderDate, double totalAmount, String buyerFullname, String buyerPhone, String buyerAddress) {
        this.id = id;
        this.customerID = customerID;
        this.orderStatusID = orderStatusID;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.buyerFullname = buyerFullname;
        this.buyerPhone = buyerPhone;
        this.buyerAddress = buyerAddress;
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

    public Object getOrderStatusID() {
        return orderStatusID;
    };

    public void setOrderStatusID(Object orderStatusID) {
        this.orderStatusID = orderStatusID;
    };

    public Timestamp getOrderDate() {
        return orderDate;
    };

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    };

    public double getTotalAmount() {
        return totalAmount;
    };

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    };

    public String getBuyerFullname() {
        return buyerFullname;
    };

    public void setBuyerFullname(String buyerFullname) {
        this.buyerFullname = buyerFullname;
    };

    public String getBuyerPhone() {
        return buyerPhone;
    };

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    };

    public String getBuyerAddress() {
        return buyerAddress;
    };

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    };

}