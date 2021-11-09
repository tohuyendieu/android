package fptu.ninhtbm.thebookshop.ui.checkout.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Cart;
import fptu.ninhtbm.thebookshop.model.Order;
import fptu.ninhtbm.thebookshop.model.OrderItem;
import fptu.ninhtbm.thebookshop.model.Stock;
import fptu.ninhtbm.thebookshop.ui.base.BasePresenter;
import fptu.ninhtbm.thebookshop.ui.checkout.ICheckoutActivity;

public class CheckoutPresenter extends BasePresenter<ICheckoutActivity> implements ICheckoutPresenter {
    public CheckoutPresenter(ICheckoutActivity activity) {
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

    @Override
    public void checkoutCart (List<BookSelected> bookSelectedList, Order order) {
        Task t1 = db.collection("Book").get();
        Task t2 = db.collection("Stock").get();

        // Check book in stock is available or not to checkout,
        Task combinedTask = Tasks.whenAllSuccess(t1, t2).addOnSuccessListener(list -> {
            // Get data from firestore return with object
            List<DocumentSnapshot> bookDocList = ((QuerySnapshot) list.get(0)).getDocuments();
            Map<String, Object> bookMap = new HashMap<>();
            for (int i = 0; i < bookDocList.size(); i++) {
                Book book = (Book) bookDocList.get(i).toObject(Book.class);
                book.setId(bookDocList.get(i).getId());
                bookMap.put(book.getId(), book);
            }

            List<DocumentSnapshot> stockDocList = ((QuerySnapshot) list.get(1)).getDocuments();
            Map<String, Object> stockMap = new HashMap<>();
            for (int i = 0; i < stockDocList.size(); i++) {
                Stock stock = (Stock) stockDocList.get(i).toObject(Stock.class);
                stock.setId(stockDocList.get(i).getId());
                stockMap.put(stock.getId(), stock);
            }

            boolean canCheckout = true;
            for (int i = 0; i < bookSelectedList.size(); i++) {
                BookSelected item = bookSelectedList.get(i);
                String bookId = ((Book) item.getBookID()).getId();
                Book book = (Book) bookMap.get(bookId);
                Stock stock = (Stock) stockMap.get(((DocumentReference) book.getStockID()).getId());
                if (item.getQuantity() > stock.getQuantity()) {
//                        Log.d(TAG, "Số lượng sách ID: " + book.getId() + " trong kho không đủ.");
                    canCheckout = false;
                    break;
                }
            }

            if (canCheckout) {
                // Thêm order mới và orderItem mới
                addOrderAndOrderItem(bookSelectedList, order);

                // Cập nhật totalBookSelled cho Book và quantity cho Stock
                updateQuantityBook(bookSelectedList);

                // Xóa các item trong cart vừa checkout
                for (int i = 0; i < bookSelectedList.size(); i++) {
                    BookSelected item = bookSelectedList.get(i);
                    deleteBookSelectedByID(item.getId());
                }
                // Check out done
                mActivity.checkoutSuccess();
            } else {
                mActivity.popSnackbarNotification(R.string.txt_not_enough_book_in_stock);
            }
        }).addOnFailureListener(e -> mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure));
    }
    // Delete BookSelected by ID
    private void deleteBookSelectedByID (String bookSelectedID) {
        db.collection("BookSelected").document(bookSelectedID)
                .delete()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    }
                });
    }
    // Add new doc with autogenerate ID with orderConverter
    private void addOrderAndOrderItem (List<BookSelected> bookSelectedList, Order order) {
        Map<String, Object> orderAdd = new HashMap<>();
        orderAdd.put("customerID", order.getCustomerID());
        orderAdd.put("orderStatusID", order.getOrderStatusID());
        orderAdd.put("orderDate", order.getOrderDate());
        orderAdd.put("totalAmount", order.getTotalAmount());
        orderAdd.put("buyerFullname", order.getBuyerFullname());
        orderAdd.put("buyerPhone", order.getBuyerPhone());
        orderAdd.put("buyerAddress", order.getBuyerAddress());

        db.collection("Order")
                .add(orderAdd)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentReference orderDoc = task.getResult();
//                            Log.d(TAG, "Added Order with ID: " + orderDoc.getId());

                        // Thêm nhiều orderitem mới
                        for (int i = 0; i < bookSelectedList.size(); i++) {
                            BookSelected item = bookSelectedList.get(i);
                            OrderItem orderItem = new OrderItem(db.collection("Book").document(((Book)item.getBookID()).getId()), db.collection("Order").document(orderDoc.getId()), item.getQuantity(), ((Book)item.getBookID()).getPrice(), ((Book)item.getBookID()).getDiscount());
                            addOrderItem(orderItem);
                        }
                    }
                });
    }

    private void addOrderItem (OrderItem orderItem) {
        Map<String, Object> orderItemAdd = new HashMap<>();
        orderItemAdd.put("bookID", orderItem.getBookID());
        orderItemAdd.put("orderID", orderItem.getOrderID());
        orderItemAdd.put("quantity", orderItem.getQuantity());
        orderItemAdd.put("sellPrice", orderItem.getSellPrice());
        orderItemAdd.put("discount", orderItem.getDiscount());
        db.collection("OrderItem")
                .add(orderItemAdd)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentReference orderItemDoc = task.getResult();
//                            Log.d(TAG, "Added OrderItem with ID: " + orderItemDoc.getId());
                    }
                });
    }

    // Update totalBookSelled with each book checkout and quantity in Stock
    private void updateQuantityBook (List<BookSelected> bookSelectedList) {
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            List<Book> newBookUpdate = new ArrayList<>();
            for (int i = 0; i < bookSelectedList.size(); i++) {
                BookSelected item = bookSelectedList.get(i);
                DocumentReference bookDocRef = db.collection("Book").document(((Book)item.getBookID()).getId());
                DocumentSnapshot bookDoc = transaction.get(bookDocRef);
                long prevTotalBookSelled = (bookDoc.getLong("totalBookSelled") != null) ? bookDoc.getLong("totalBookSelled") : 0;

                DocumentReference stockRef = bookDoc.getDocumentReference("stockID");
                DocumentSnapshot stockDoc = transaction.get(stockRef);
                long prevQuantity = (stockDoc.getLong("quantity") != null) ? stockDoc.getLong("quantity") : 0;

                Book bookUpdate = new Book();
                bookUpdate.setId(bookDocRef.getId());
                bookUpdate.setTotalBookSelled((int)prevTotalBookSelled + item.getQuantity());
                Stock stock = new Stock();
                stock.setId(stockRef.getId());
                stock.setQuantity((int)prevQuantity - item.getQuantity());
                bookUpdate.setStockID(stock);

                newBookUpdate.add(bookUpdate);
            }

            // Update new totalBookSelled for Books and quantity for Stocks
            for (int i = 0; i < newBookUpdate.size(); i++) {
                Book item = newBookUpdate.get(i);
                transaction.update(db.collection("Book").document(item.getId()), "totalBookSelled", item.getTotalBookSelled() );
//                    Log.d(TAG, "Updated Book ID " + item.getId());

                transaction.update(db.collection("Stock").document(((Stock)item.getStockID()).getId()), "quantity", ((Stock)item.getStockID()).getQuantity(), "updatedDate", new Timestamp(new Date()));
//                    Log.d(TAG, "Updated Stock ID " + ((Stock)item.getStockID()).getId());
            }

            // Success
            return null;
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "Transaction update totalBookSelled successfully committed!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Transaction update totalBookSelled failed: ", e);
            }
        });
    }

}
