package fptu.ninhtbm.thebookshop.ui.cart.presenter;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Cart;
import fptu.ninhtbm.thebookshop.ui.base.BasePresenter;
import fptu.ninhtbm.thebookshop.ui.cart.ICartActivity;

public class CartPresenter extends BasePresenter<ICartActivity> implements ICartPresenter {
    public CartPresenter(ICartActivity activity) {
        super(activity);
    }

    @Override
    public void loadAllBookInCart(String customerID) {
        db.collection("Cart")
                .whereEqualTo("customerID", db.collection("Customer").document(customerID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot cartDocs = task.getResult();
                        if(cartDocs.getDocuments().size() > 0) {
                            Cart cart = cartDocs.getDocuments().get(0).toObject(Cart.class);
                            cart.setId(cartDocs.getDocuments().get(0).getId());
//                                Log.d(TAG, "Customer has cart withID: " + cart.getId());

                            db.collection("BookSelected")
                                    .whereEqualTo("cartID", db.collection("Cart").document(cart.getId()))
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            QuerySnapshot bookSelectedDocs = task1.getResult();
                                            List<BookSelected> bookSelectedList = new ArrayList<>();
                                            if( bookSelectedDocs.getDocuments().size() == 0){
                                                bookSelectedList.sort(Comparator.comparing(BookSelected::getId));
                                                mActivity.loadBookSelectedInCart(bookSelectedList);
                                                return;
                                            }
                                            for (int i = 0; i < bookSelectedDocs.getDocuments().size(); i++) {
                                                final int index = i;
                                                BookSelected bookSelected = bookSelectedDocs.getDocuments().get(index).toObject(BookSelected.class);
                                                bookSelected.setId(bookSelectedDocs.getDocuments().get(index).getId());
                                                ((DocumentReference)bookSelected.getBookID())
                                                        .get()
                                                        .addOnCompleteListener(task11 -> {
                                                            if(task11.isSuccessful()){
                                                                DocumentSnapshot doc = task11.getResult();
                                                                if(doc.exists()){
                                                                    Book book = doc.toObject(Book.class);
                                                                    book.setId(doc.getId());
                                                                    bookSelected.setBookID(book);

                                                                    bookSelectedList.add(bookSelected);
//                                                                                Log.d(TAG, "BookSelected: " + bookSelected.toString());
                                                                } else {
                                                                    mActivity.popSnackbarNotification(R.string.txt_cart_not_found);
                                                                }
                                                            } else {
                                                                mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                                                            }
                                                            // Return data if end for loop
                                                            if(index == bookSelectedDocs.getDocuments().size() - 1) {
                                                                bookSelectedList.sort(Comparator.comparing(BookSelected::getId));
                                                                mActivity.loadBookSelectedInCart(bookSelectedList);
//                                                                            ( (Button)findViewById(R.id.btnTestFirestore)).setText(bookSelectedList.size() + "xx");

//                                                                            double totalAmount = 0;
//                                                                            for (int i = 0; i < bookSelectedList.size(); i++) {
//                                                                                totalAmount += bookSelectedList.get(i).getQuantity() * ((Book)bookSelectedList.get(i).getBookID()).getPrice() * (1 - (((Book)bookSelectedList.get(i).getBookID()).getDiscount() / 100));
//                                                                            }
//
//                                                                            checkoutCart(bookSelectedList, new Order(db.collection("Customer").document("AnBBxrKJHzceljqhhTtr"), db.collection("OrderStatus").document("cSqT4qJKoZjMdvdVNSvi"), new Timestamp(new Date()), totalAmount, "King Wisdom", "0337220922", "Hoa Lac City"));
                                                            }
                                                        });
                                            }
                                        } else {
                                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                                        }
                                    });
                        } else {
                            mActivity.popSnackbarNotification(R.string.txt_cart_not_found);
                        }
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    }
                });
    }
    // Update quantity of BookSelected record with quanity = previousQuantity + 1;
    public void updateQuantityBookSelected (BookSelected bookSelected, int quantityIncrease) {
        int newQuantity = bookSelected.getQuantity() + quantityIncrease;
        if(newQuantity > 0){
            db.collection("BookSelected").document(bookSelected.getId())
                    .update("quantity", newQuantity)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            mActivity.popSnackbarNotification(R.string.txt_quantity_change_success);
                        } else {
                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                        }
                        mActivity.reloadData();
                    });
        } else{
            deleteBookSelectedById(bookSelected.getId());
        }
    }

    // Delete BookSelected by ID
    public void deleteBookSelectedById (String bookSelectedID) {
        db.collection("BookSelected").document(bookSelectedID)
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        mActivity.popSnackbarNotification(R.string.txt_remove_book_selected_success);
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    }
                    mActivity.reloadData();
                });
    }
}
