package fptu.ninhtbm.thebookshop.models;

import com.google.firebase.Timestamp;

public class Author {
    private String id;
    private String name;
    private Timestamp dob;
    private Timestamp createdAt;

    public Author() {
    }

    public Author(String id, String name, Timestamp dob, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.dob = dob;
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

    public Timestamp getDob() {
        return dob;
    };

    public void setDob(Timestamp dob) {
        this.dob = dob;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", createdAt=" + createdAt +
                '}';
    }
}