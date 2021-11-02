package fptu.ninhtbm.thebookshop.model;

import com.google.firebase.Timestamp;

public class Account {
    private String id;
    private String username;
    private String password;
    private boolean status;
    private Timestamp createdAt;

    public Account() {
    }

    public Account(String id, String username, String password, boolean status, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
