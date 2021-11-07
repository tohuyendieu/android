package fptu.ninhtbm.thebookshop.ui.bookdetail.presenter;

public interface IBookDetailPresenter {
    void loadBookById(String bookId);

    void addBookToCart(String customerId, String bookId);

    void rateBook(String bookId, int rateStar);

    void getAllAuthorByBookId(String bookId);
}
