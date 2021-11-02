package fptu.ninhtbm.thebookshop.models;

public class AuthorBook {
    private String authorID;
    private String bookID;

    public AuthorBook() {
    }

    public AuthorBook(String authorID, String bookID) {
        this.authorID = authorID;
        this.bookID = bookID;
    }

    public String getAuthorID() {
        return authorID;
    };

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    };

    public String getBookID() {
        return bookID;
    };

    public void setBookID(String bookID) {
        this.bookID = bookID;
    };

    @Override
    public String toString() {
        return "AuthorBook{" +
                "authorID='" + authorID + '\'' +
                ", bookID='" + bookID + '\'' +
                '}';
    }
}