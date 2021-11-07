package fptu.ninhtbm.thebookshop.ui.bookdetail;

import java.util.List;

import fptu.ninhtbm.thebookshop.model.Author;
import fptu.ninhtbm.thebookshop.model.Book;

public interface IBookDetailActivity {
    void loadBook(Book book);
    void popSnackbarNotification(int messageResId);
    void ratedSuccess();
    void loadAuthors(List<Author> authorList);

}
