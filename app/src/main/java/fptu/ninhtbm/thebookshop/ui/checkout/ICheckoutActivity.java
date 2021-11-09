package fptu.ninhtbm.thebookshop.ui.checkout;

import java.util.List;

import fptu.ninhtbm.thebookshop.model.BookSelected;

public interface ICheckoutActivity {
    void popSnackbarNotification(int messageResId);
    void loadBookSelectedInCart(List<BookSelected> bookSelectedList);
    void checkoutSuccess();
}
