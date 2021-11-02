package fptu.ninhtbm.thebookshop.model;

public class OrderStatus {
    private String id;
    private String status;

    public OrderStatus() {
    }

    public OrderStatus(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    };

    public String getStatus() {
        return status;
    };

    public void setStatus(String status) {
        this.status = status;
    };

    @Override
    public String toString() {
        return "OrderStatus{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}