package fptu.ninhtbm.thebookshop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Category implements Parcelable {
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

    protected Category(Parcel in) {
        id = in.readString();
        title = in.readString();
        createdAt = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeParcelable(createdAt, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}