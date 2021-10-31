package fptu.ninhtbm.thebookshop.ui.models;

import com.google.firebase.Timestamp;

public class Category {
    private String id;
    private String title;
    private Timestamp createdAt;

    public Category() {
    }

    public Category(String id, String title, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    };

    public String getTitle() {
        return title;
    };

    public void setTitle(String title) {
        this.title = title;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

}