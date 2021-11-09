package fptu.ninhtbm.thebookshop.ui.cart.presenter;

import fptu.ninhtbm.thebookshop.model.BookSelected;

public interface ICartPresenter {
    void loadAllBookInCart(String customerID);
    void updateQuantityBookSelected(BookSelected bookSelected, int quantity);
    void deleteBookSelectedById(String bookSelectedID);
}
