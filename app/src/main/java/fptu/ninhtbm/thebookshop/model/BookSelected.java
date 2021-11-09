package fptu.ninhtbm.thebookshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class BookSelected implements Parcelable {
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

    protected BookSelected(Parcel in) {
        id = in.readString();
        quantity = in.readInt();
        createdAt = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(quantity);
        dest.writeParcelable(createdAt, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookSelected> CREATOR = new Creator<BookSelected>() {
        @Override
        public BookSelected createFromParcel(Parcel in) {
            return new BookSelected(in);
        }

        @Override
        public BookSelected[] newArray(int size) {
            return new BookSelected[size];
        }
    };

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