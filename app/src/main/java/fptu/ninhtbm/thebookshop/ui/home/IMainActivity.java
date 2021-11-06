package fptu.ninhtbm.thebookshop.ui.home;

import java.util.List;

import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.Category;

public interface IMainActivity {
    void popSnackbarNotification(int messageResId);

    void loadCategories(List<Category> categoryList);

    void loadTopBook(String sortType, List<Book> bookList);
}
