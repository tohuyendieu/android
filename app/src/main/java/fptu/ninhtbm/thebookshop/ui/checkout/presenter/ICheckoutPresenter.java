package fptu.ninhtbm.thebookshop.ui.checkout.presenter;

import java.util.List;

import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Order;

public interface ICheckoutPresenter {
    void loadAllBookInCart(String customerID);
    void checkoutCart(List<BookSelected> bookSelectedList, Order order);
}
