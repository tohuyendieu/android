package fptu.ninhtbm.thebookshop.model;

import com.google.firebase.Timestamp;

public class Customer {
    private String id;
    private Object accountID;
    private String name;
    private String address;
    private String mail;
    private String phone;
    private Timestamp createdAt;

    public Customer() {
    }

    public Customer(String id, Object accountID, String name, String address, String mail, String phone, Timestamp createdAt) {
        this.id = id;
        this.accountID = accountID;
        this.name = name;
        this.address = address;
        this.mail = mail;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public Customer(Object accountID, String name, String address, String mail, String phone) {
        this.accountID = accountID;
        this.name = name;
        this.address = address;
        this.mail = mail;
        this.phone = phone;
    }

    public Customer(String name, String address, String mail, String phone) {
        this.name = name;
        this.address = address;
        this.mail = mail;
        this.phone = phone;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    };

    public Object getAccountID() {
        return accountID;
    };

    public void setAccountID(Object accountID) {
        this.accountID = accountID;
    };

    public String getName() {
        return name;
    };

    public void setName(String name) {
        this.name = name;
    };

    public String getAddress() {
        return address;
    };

    public void setAddress(String address) {
        this.address = address;
    };

    public String getMail() {
        return mail;
    };

    public void setMail(String mail) {
        this.mail = mail;
    };

    public String getPhone() {
        return phone;
    };

    public void setPhone(String phone) {
        this.phone = phone;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", accountID=" + accountID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}