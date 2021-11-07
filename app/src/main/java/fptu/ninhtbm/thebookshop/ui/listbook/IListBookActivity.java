package fptu.ninhtbm.thebookshop.ui.listbook;

import java.util.List;

import fptu.ninhtbm.thebookshop.model.Book;

public interface IListBookActivity {
    void popSnackbarNotification(int messageResId);
    void loadBooks(List<Book> bookList);
}
