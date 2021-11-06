package fptu.ninhtbm.thebookshop.model;

public class AuthorBook {
    private String id;
    private Object authorID;
    private Object bookID;

    public AuthorBook() {
    }

    public AuthorBook(String authorID, String bookID) {
        this.authorID = authorID;
        this.bookID = bookID;
    }

    public AuthorBook(String id, String authorID, String bookID) {
        this.id = id;
        this.authorID = authorID;
        this.bookID = bookID;
    }

    public Object getAuthorID() {
        return authorID;
    };

    public void setAuthorID(Object authorID) {
        this.authorID = authorID;
    };

    public Object getBookID() {
        return bookID;
    };

    public void setBookID(Object bookID) {
        this.bookID = bookID;
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AuthorBook{" +
                "id='" + id + '\'' +
                ", authorID=" + authorID +
                ", bookID=" + bookID +
                '}';
    }
}