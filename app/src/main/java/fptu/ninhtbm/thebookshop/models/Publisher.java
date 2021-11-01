package fptu.ninhtbm.thebookshop.models;

import com.google.firebase.Timestamp;

public class Publisher {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String mail;
    private Timestamp createdAt;

    public Publisher() {
    }

    public Publisher(String id, String name, String address, String phone, String mail, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.mail = mail;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    };

    public void setPhone(String phone) {
        this.phone = phone;
    };

    public String getMail() {
        return mail;
    };

    public void setMail(String mail) {
        this.mail = mail;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

    @Override
    public String toString() {
        return "Publisher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}