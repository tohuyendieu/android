package fptu.ninhtbm.thebookshop.ui.cart;

import java.util.List;

import fptu.ninhtbm.thebookshop.model.BookSelected;

public interface ICartActivity {
    void popSnackbarNotification(int messageResId);
    void loadBookSelectedInCart(List<BookSelected> bookSelectedList);
    void reloadData();
}
