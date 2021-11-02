package fptu.ninhtbm.thebookshop.model;

import com.google.firebase.Timestamp;

public class Book {
    private String id;
    private Object publisherID;
    private Object stockID;
    private Object categoryID;
    private String title;
    private double price;
    private double discount;
    private int totalPage;
    private String bookCoverImg;
    private String description;
    private int totalRating;
    private int totalRatingStar;
    private int totalBookSelled;
    private double avgRated;
    private Timestamp createdAt;

    public Book() {
    }

    public Book(String id, Object publisherID, Object stockID, Object categoryID, String title, double price, double discount, int totalPage, String bookCoverImg, String description, int totalRating, int totalRatingStar, int totalBookSelled, double avgRated, Timestamp createdAt) {
        this.id = id;
        this.publisherID = publisherID;
        this.stockID = stockID;
        this.categoryID = categoryID;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.totalPage = totalPage;
        this.bookCoverImg = bookCoverImg;
        this.description = description;
        this.totalRating = totalRating;
        this.totalRatingStar = totalRatingStar;
        this.totalBookSelled = totalBookSelled;
        this.avgRated = avgRated;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    };

    public void setId(String id) {
        this.id = id;
    };

    public Object getPublisherID() {
        return publisherID;
    };

    public void setPublisherID(Object publisherID) {
        this.publisherID = publisherID;
    };

    public Object getStockID() {
        return stockID;
    };

    public void setStockID(Object stockID) {
        this.stockID = stockID;
    };

    public Object getCategoryID() {
        return categoryID;
    };

    public void setCategoryID(Object categoryID) {
        this.categoryID = categoryID;
    };

    public String getTitle() {
        return title;
    };

    public void setTitle(String title) {
        this.title = title;
    };

    public double getPrice() {
        return price;
    };

    public void setPrice(double price) {
        this.price = price;
    };

    public double getDiscount() {
        return discount;
    };

    public void setDiscount(double discount) {
        this.discount = discount;
    };

    public int getTotalPage() {
        return totalPage;
    };

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    };

    public String getBookCoverImg() {
        return bookCoverImg;
    };

    public void setBookCoverImg(String bookCoverImg) {
        this.bookCoverImg = bookCoverImg;
    };

    public String getDescription() {
        return description;
    };

    public void setDescription(String description) {
        this.description = description;
    };

    public int getTotalRating() {
        return totalRating;
    };

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    };

    public int getTotalRatingStar() {
        return totalRatingStar;
    };

    public void setTotalRatingStar(int totalRatingStar) {
        this.totalRatingStar = totalRatingStar;
    };

    public int getTotalBookSelled() {
        return totalBookSelled;
    };

    public void setTotalBookSelled(int totalBookSelled) {
        this.totalBookSelled = totalBookSelled;
    };

    public double getAvgRated() {
        return avgRated;
    };

    public void setAvgRated(double avgRated) {
        this.avgRated = avgRated;
    };

    public Timestamp getCreatedAt() {
        return createdAt;
    };

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    };

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", publisherID=" + publisherID +
                ", stockID=" + stockID +
                ", categoryID=" + categoryID +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", totalPage=" + totalPage +
                ", bookCoverImg='" + bookCoverImg + '\'' +
                ", description='" + description + '\'' +
                ", totalRating=" + totalRating +
                ", totalRatingStar=" + totalRatingStar +
                ", totalBookSelled=" + totalBookSelled +
                ", avgRated=" + avgRated +
                ", createdAt=" + createdAt +
                '}';
    }
}