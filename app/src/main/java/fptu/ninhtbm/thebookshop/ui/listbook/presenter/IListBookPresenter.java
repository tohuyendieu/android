package fptu.ninhtbm.thebookshop.ui.listbook.presenter;

public interface IListBookPresenter {
    void loadBookByCategoryId(String categoryId);

    void searchBooksByTitle (String keyword);
}
