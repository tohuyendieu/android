package fptu.ninhtbm.thebookshop.ui.bookdetail.presenter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Author;
import fptu.ninhtbm.thebookshop.model.AuthorBook;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Cart;
import fptu.ninhtbm.thebookshop.model.Category;
import fptu.ninhtbm.thebookshop.model.Publisher;
import fptu.ninhtbm.thebookshop.model.Stock;
import fptu.ninhtbm.thebookshop.ui.base.BasePresenter;
import fptu.ninhtbm.thebookshop.ui.bookdetail.IBookDetailActivity;

public class BookDetailPresenter extends BasePresenter<IBookDetailActivity> implements IBookDetailPresenter {

    public BookDetailPresenter(IBookDetailActivity activity) {
        super(activity);
    }

    @Override
    public void loadBookById(String bookId) {
        final DocumentReference docRef = db.collection("Book").document(bookId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Book book = snapshot.toObject(Book.class);
                    book.setId(snapshot.getId());

                    DocumentReference categoryRef = (DocumentReference) book.getCategoryID();
                    DocumentReference publisherRef = (DocumentReference) book.getPublisherID();
                    DocumentReference stockRef = (DocumentReference) book.getStockID();

                    Task t1 = categoryRef.get();
                    Task t2 = publisherRef.get();
                    Task t3 = stockRef.get();

                    Task combinedTask = Tasks.whenAllSuccess(t1, t2, t3).addOnSuccessListener(list -> {
                        // Get data from firestore return with object
                        Category category = ((DocumentSnapshot) list.get(0)).toObject(Category.class);
                        category.setId(((DocumentSnapshot) list.get(0)).getId());
                        Publisher publisher = ((DocumentSnapshot) list.get(1)).toObject(Publisher.class);
                        publisher.setId(((DocumentSnapshot) list.get(1)).getId());
                        Stock stock = ((DocumentSnapshot) list.get(2)).toObject(Stock.class);
                        stock.setId(((DocumentSnapshot) list.get(2)).getId());

                        book.setCategoryID(category);
                        book.setPublisherID(publisher);
                        book.setStockID(stock);

                        mActivity.loadBook(book);
                    }).addOnFailureListener(e1 -> mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure));
                } else {
                    mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                }
            }
        });
    }

    @Override
    public void addBookToCart(String customerId, String bookId) {
        db.collection("Cart")
                .whereEqualTo("customerID", db.collection("Customer").document(customerId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot cartDocs = task.getResult();
                        if (cartDocs.getDocuments().size() > 0) {
                            Cart cart = cartDocs.getDocuments().get(0).toObject(Cart.class);
                            cart.setId(cartDocs.getDocuments().get(0).getId());
//                            Log.d(TAG, "Đã tồn tại Cart with ID: " + cart.getId());

                            db.collection("BookSelected")
                                    .whereEqualTo("cartID", db.collection("Cart").document(cart.getId()))
                                    .whereEqualTo("bookID", db.collection("Book").document(bookId))
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot bookSelectedDocs = task1.getResult();
                                            if (bookSelectedDocs.getDocuments().size() > 0) {
                                                BookSelected bookSelected = bookSelectedDocs.getDocuments().get(0).toObject(BookSelected.class);
                                                bookSelected.setId(bookSelectedDocs.getDocuments().get(0).getId());
//                                                    Log.d(TAG, "BookSelected đã tồn tại với ID: " + bookSelected.getId());
                                                updateQuantityBookSelected(bookSelected, 1);
                                            } else {
//                                                    Log.d(TAG, "Chưa tồn tại BookSelected");
                                                BookSelected bookSelected = new BookSelected(db.collection("Book").document(bookId), db.collection("Cart").document(cart.getId()), 1, new Timestamp(new Date()));

                                                addBookSelected(bookSelected).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task1) {
                                                        if (task1.isSuccessful()) {
                                                            mActivity.popSnackbarNotification(R.string.txt_add_to_cart_success);
                                                        } else {
                                                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                                        }
                                    });
                        } else {
//                            Log.d(TAG, "Chưa tồn tại Cart");
                            Cart newCart = new Cart(db.collection("Customer").document(customerId), new Timestamp(new Date()));

                            addCart(newCart)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                String cartIDAdded = task.getResult().getId();
//                                                Log.d(TAG, "Added new Cart with ID: " + cartIDAdded);
                                                BookSelected bookSelected = new BookSelected(db.collection("Book").document(bookId), db.collection("Cart").document(cartIDAdded), 1, new Timestamp(new Date()));

                                                addBookSelected(bookSelected).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            mActivity.popSnackbarNotification(R.string.txt_add_to_cart_success);
                                                        } else {
                                                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                                                        }
                                                    }
                                                });
                                            } else {
                                                mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                                            }
                                        }
                                    });
                        }
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    }
                });
    }

    @Override
    public void rateBook(String bookId, int ratedStar) {

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentReference bookDocRef = db.collection("Book").document(bookId);
            DocumentSnapshot bookSnapshot = transaction.get(bookDocRef);
            long prevTotalRating = (bookSnapshot.getLong("totalRating") != null) ? bookSnapshot.getLong("totalRating") : 0;
            long prevTotalRatingStar = (bookSnapshot.getLong("totalRatingStar") != null) ? bookSnapshot.getLong("totalRatingStar") : 0;
            long newTotalRating = prevTotalRating + 1;
            long newTotalRatingStar = prevTotalRatingStar + ratedStar;
            transaction.update(bookDocRef, "totalRating", newTotalRating, "totalRatingStar", newTotalRatingStar, "avgRated", (double) newTotalRatingStar / newTotalRating);
//                Log.d(TAG, "Updated Rated for Book with BookID: " + bookId);
            // Success
            return null;
        })
                .addOnSuccessListener(aVoid -> {
                    mActivity.ratedSuccess();
                })
                .addOnFailureListener(e -> mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure));
    }

    @Override
    public void getAllAuthorByBookId(String bookId) {
        db.collection("AuthorBook")
                .whereEqualTo("bookID", db.collection("Book").document(bookId))
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot authorBookDocs = task.getResult();
                        if(authorBookDocs.getDocuments().size() > 0) {
                            List<Author> authorList = new ArrayList<>();

                            for (int i = 0; i < authorBookDocs.getDocuments().size(); i++) {
                                final int index = i;
                                AuthorBook authorBook = authorBookDocs.getDocuments().get(index).toObject(AuthorBook.class);
                                authorBook.setId(authorBookDocs.getDocuments().get(index).getId());
                                ((DocumentReference)authorBook.getAuthorID())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()){
                                                DocumentSnapshot doc = task1.getResult();
                                                if(doc.exists()){
                                                    Author author = doc.toObject(Author.class);
                                                    author.setId(doc.getId());

                                                    authorList.add(author);
                                                } else {
                                                    mActivity.popSnackbarNotification(R.string.txt_author_not_found);
                                                }
                                            } else {
                                                mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                                            }
                                            // Return data if end for loop
                                            if(index == authorBookDocs.getDocuments().size() - 1) {
                                                mActivity.loadAuthors(authorList);
                                            }
                                        });
                            }
                        } else {
                            mActivity.popSnackbarNotification(R.string.txt_author_not_found);
                        }
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    }
                });
    }


    private void updateQuantityBookSelected(BookSelected bookSelected, int quantityIncrease) {
        int newQuantity = bookSelected.getQuantity() + quantityIncrease;
        if (newQuantity > 0) {
            db.collection("BookSelected").document(bookSelected.getId())
                    .update("quantity", newQuantity)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mActivity.popSnackbarNotification(R.string.txt_update_cart_success);
                        } else {
                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                        }
                    });
        } else {
            deleteBookSelectedByID(bookSelected.getId());
        }


    }

    // Delete BookSelected by ID
    private void deleteBookSelectedByID(String bookSelectedID) {
        db.collection("BookSelected").document(bookSelectedID)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //
                        } else {
                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                        }
                    }
                });
    }

    private Task<DocumentReference> addBookSelected(BookSelected bookSelected) {
        Map<String, Object> bookSelectedAdd = new HashMap<>();
        bookSelectedAdd.put("bookID", bookSelected.getBookID());
        bookSelectedAdd.put("cartID", bookSelected.getCartID());
        bookSelectedAdd.put("quantity", bookSelected.getQuantity());
        bookSelectedAdd.put("createdAt", bookSelected.getCreatedAt());

        // Add new BookSelected has bookID and cartID
        return db.collection("BookSelected").add(bookSelectedAdd);
    }


    // Add new Cart doc autogenerate ID with converter
    private Task<DocumentReference> addCart(Cart cart) {
        Map<String, Object> cartAdd = new HashMap<>();
        cartAdd.put("customerID", cart.getCustomerID());
        cartAdd.put("createdAt", cart.getCreatedAt());

        // Add new Cart for Customer
        return db.collection("Cart").add(cartAdd);
    }
}