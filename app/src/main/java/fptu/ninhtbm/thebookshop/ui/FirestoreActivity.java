package fptu.ninhtbm.thebookshop.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Account;
import fptu.ninhtbm.thebookshop.model.AuthorBook;
import fptu.ninhtbm.thebookshop.model.Author;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Cart;
import fptu.ninhtbm.thebookshop.model.Category;
import fptu.ninhtbm.thebookshop.model.Comment;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.model.Order;
import fptu.ninhtbm.thebookshop.model.OrderItem;
import fptu.ninhtbm.thebookshop.model.Publisher;
import fptu.ninhtbm.thebookshop.model.Stock;

public class FirestoreActivity extends AppCompatActivity {
    FirebaseFirestore db;
    static final String TAG = "Firestore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        db = FirebaseFirestore.getInstance();
    }

    public void onTestFirestore(View view) {
        // SOME METHOD TO CALL FIRESTORE WITH EACH ROUTING
//        getTopBookByField(5, "totalBookSelled");      // Rounting 1
//        getTopBookByField(5, "createdAt");            // Rounting 1
//        getTopBookByField(5, "avgRated");             // Rounting 1
//        getTopBookByField(5, "discount");             // Rounting 1

//        getCustomerByID("4yMOdgrzNcdLU2quUk7A"); // Rounting 2

//         updateCustomer("4yMOdgrzNcdLU2quUk7A", new Customer(db.collection("Account").document("Uzmf3Mj04ugmGU7kb0eb"), "Trương Thị Huệ", "số 64b-Lê Văn Chí-Linh Trung-Thủ Đức-Tp.Hồ Chí Minh", "hue@gmail.com", "0937249516")); // Rounting 2.1

//         getBookByID("0oRxIqalaGZGgWl6H4Ld"); // Rounting 3

//         addComment(new Comment(db.collection("Book").document("6jx04F0EkJp4APEanHau"), db.collection("Customer").document("4yMOdgrzNcdLU2quUk7A"), 2, new Timestamp(new Date()))); // Rounting 3
//        updateBookRated("0oRxIqalaGZGgWl6H4Ld" , 5);
//        updateComment("dUAauOA7orbWTiqtPI9Q", 3); // Rounting 3
//        getCommentedInBook("0oRxIqalaGZGgWl6H4Ld" ,"4yMOdgrzNcdLU2quUk7A");   //Rounting 3
//        addBookSelected(new BookSelected(db.collection("Book").document("123"), db.collection("Cart").document("234"), 1, new Timestamp(new Date())));
//        addBookToCart("AnBBxrKJHzceljqhhTtr", "5b1RnyIOPla1RvNw4cip");    //Rounting 3
//        getAllBookSelectedByCustomerID("AnBBxrKJHzceljqhhTtr");       //Rounting 3
        getAllAuthorByBookID("0oRxIqalaGZGgWl6H4Ld");

//        getAllBookByCategoryID("zna5C9ZCKki5V8L97LZn");  // Rounting 5
//        getAllCategory();     // Rounting 5

//        getCustomerInfoByLogin("account13", "account13");  // Rounting 7

//        addAccountAndCustomerInfo(new Account("King123", "123456"), new Customer("King Wisdom", "Hòa Lạc",  "kingwisdom.dev@gmail.com", "0337220922"));  // Rounting 8

//        List<BookSelected> bookSelectedList = new ArrayList<>(); ..... data lấy từ cart
//        double totalAmount = 0;
//        for (int i = 0; i < bookSelectedList.size(); i++) {
//            totalAmount += bookSelectedList.get(i).getQuantity() * ((Book)bookSelectedList.get(i).getBookID()).getPrice() * (1 - (((Book)bookSelectedList.get(i).getBookID()).getDiscount() / 100));
//        }
//
//        checkoutCart(bookSelectedList, new Order(db.collection("Customer").document("AnBBxrKJHzceljqhhTtr"), db.collection("OrderStatus").document("cSqT4qJKoZjMdvdVNSvi"), new Timestamp(new Date()), totalAmount, "King Wisdom", "0337220922", "Hoa Lac City")); //Rounting 9

//        updatePassword("FeuU4tA9sjd5eCQkn8UF", "123456", "account12");  // Rounting 10
    }

    private void logListData(List list){
        Log.d(TAG, "\n==================== LOG LIST DATA WITH SIZE = " + list.size() + " ====================");
        for (int i = 0; i < list.size(); i++){
            Log.d(TAG, "Item " + i + " : " + list.get(i).toString());
        }
    }

    // ====================================> Rounting 1: Home Page <==========================================
    // fieldSort = "totalBookSelled" => Get top X Best Seller Book
    // fieldSort = "createdAt" => Get top X New Book Created
    // fieldSort = "avgRated" => Get top X Rated Book
    // fieldSort = "discount" => Get top X Discount Book
    private void getTopBookByField (int topBook, String fieldSort) {
        Log.d(TAG, "getTopBookByField top " + topBook + " by " + fieldSort);
        CollectionReference docRef = db.collection("Book");
        docRef.orderBy(fieldSort, Direction.DESCENDING).limit(topBook)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Book> listBook = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                book.setId(document.getId());
                                listBook.add(book);
                            }
                            logListData(listBook);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        };

    // ====================================> Rounting 1: End Home Page <==========================================

    // ====================================> Rounting 2:  Customer Info <==========================================
    // Get Book by ID with converter
    private void getCustomerByID (String customerID) {
        final DocumentReference docRef = db.collection("Customer").document(customerID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Customer customer = snapshot.toObject(Customer.class);
                    customer.setId(snapshot.getId());
                    DocumentReference accountRef = (DocumentReference)customer.getAccountID();
                    if (accountRef != null) {
                        accountRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Account account = document.toObject(Account.class);
                                        account.setId(document.getId());
                                        customer.setAccountID(account);

                                        Log.d(TAG, "Thông tin người dùng: " + customer.toString());
                                    } else {
                                        Log.d(TAG, "Không tìm thấy thông tin người dùng!");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    // ====================================> Rounting 2: End Customer Info <==========================================

    // ====================================> Rounting 2.1: Edit Customer Info <==========================================
    // Update doc have ID is customerID with customerConverter
    private void updateCustomer (String customerID, Customer customer){
        DocumentReference customerRef = db.collection("Customer").document(customerID);

        Map<String, Object> customerUpdate = new HashMap<>();
        customerUpdate.put("name", customer.getName());
        customerUpdate.put("address",  customer.getAddress());
        customerUpdate.put("mail", customer.getMail());
        customerUpdate.put("phone", customer.getPhone());

        customerRef
                .update(customerUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    // ====================================> Rounting 2.1: End Edit Customer Info <==========================================

    // ====================================> Rounting 3: Book Detail <==========================================

    private void getAllAuthorByBookID (String bookID) {
        db.collection("AuthorBook")
                .whereEqualTo("bookID", db.collection("Book").document(bookID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot doc = task.getResult();
                                                        if(doc.exists()){
                                                            Author author = doc.toObject(Author.class);
                                                            author.setId(doc.getId());

                                                            authorList.add(author);
                                                        } else {
                                                            Log.d(TAG, "Không tìm thấy thông tin Author ID: " + ((DocumentReference)authorBook.getBookID()).getId());
                                                        }
                                                    } else {
                                                        Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                    }
                                                    // Return data if end for loop
                                                    if(index == authorBookDocs.getDocuments().size() - 1) {
                                                        logListData(authorList);
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG, "Không tìm thấy thông tin tác giả!");
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    // Get Book by ID with converter
    private void getBookByID (String bookID){
        final DocumentReference docRef = db.collection("Book").document(bookID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Book book = snapshot.toObject(Book.class);
                    book.setId(snapshot.getId());

                    DocumentReference categoryRef = (DocumentReference)book.getCategoryID();
                    DocumentReference publisherRef = (DocumentReference)book.getPublisherID();
                    DocumentReference stockRef = (DocumentReference)book.getStockID();

                    Task t1 = categoryRef.get();
                    Task t2 = publisherRef.get();
                    Task t3 = stockRef.get();

                    Task combinedTask = Tasks.whenAllSuccess(t1, t2, t3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> list) {
                            // Get data from firestore return with object
                            Category category = ((DocumentSnapshot)list.get(0)).toObject(Category.class);
                            category.setId(((DocumentSnapshot)list.get(0)).getId());
                            Publisher publisher = ((DocumentSnapshot)list.get(1)).toObject(Publisher.class);
                            publisher.setId(((DocumentSnapshot)list.get(1)).getId());
                            Stock stock = ((DocumentSnapshot)list.get(2)).toObject(Stock.class);
                            stock.setId(((DocumentSnapshot)list.get(2)).getId());

                            book.setCategoryID(category);
                            book.setPublisherID(publisher);
                            book.setStockID(stock);

                            Log.d(TAG,  book.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Có lỗi khi lấy thông tin sách.");
                        }
                    });
                } else {
                    Log.d(TAG, "Không tìm thấy thông tin của sách!");
                }
            }
        });
    }

    // Add Book to Cart with converter
    private void addBookToCart (String customerID, String bookID) {
        // If in Cart exist record of customerID else create new Cart record, then add to BookSelected with bookID, cartID, quantity
        // If Book already exist in BookSelected then update quantity of this book
        db.collection("Cart")
            .whereEqualTo("customerID", db.collection("Customer").document(customerID))
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        QuerySnapshot cartDocs = task.getResult();
                        if(cartDocs.getDocuments().size() > 0) {
                            Cart cart = cartDocs.getDocuments().get(0).toObject(Cart.class);
                            cart.setId(cartDocs.getDocuments().get(0).getId());
                            Log.d(TAG, "Đã tồn tại Cart with ID: " + cart.getId());

                            db.collection("BookSelected")
                                    .whereEqualTo("cartID", db.collection("Cart").document(cart.getId()))
                                    .whereEqualTo("bookID", db.collection("Book").document(bookID))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                QuerySnapshot bookSelectedDocs = task.getResult();
                                                 if(bookSelectedDocs.getDocuments().size() > 0) {
                                                     BookSelected bookSelected = bookSelectedDocs.getDocuments().get(0).toObject(BookSelected.class);
                                                     bookSelected.setId(bookSelectedDocs.getDocuments().get(0).getId());
                                                     Log.d(TAG, "BookSelected đã tồn tại với ID: " + bookSelected.getId());
                                                     updateQuantityBookSelected(bookSelected, 1);
                                                 } else {
                                                     Log.d(TAG, "Chưa tồn tại BookSelected");
                                                     BookSelected bookSelected = new BookSelected(db.collection("Book").document(bookID), db.collection("Cart").document(cart.getId()), 1, new Timestamp(new Date()) );

                                                     addBookSelected(bookSelected).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<DocumentReference> task) {
                                                             if(task.isSuccessful()){
                                                                 Log.d(TAG, "Added new BookSelected with ID: " + task.getResult().getId());
                                                                 Log.d(TAG, "Thêm sách vào Cart thành công!");
                                                             } else {
                                                                 Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                             }
                                                         }
                                                     });
                                                 }
                                            } else {
                                                Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Chưa tồn tại Cart");
                            Cart newCart = new Cart(db.collection("Customer").document(customerID), new Timestamp(new Date()));

                            addCart(newCart)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()){
                                                String cartIDAdded = task.getResult().getId();
                                                Log.d(TAG, "Added new Cart with ID: " + cartIDAdded);
                                                BookSelected bookSelected = new BookSelected(db.collection("Book").document(bookID), db.collection("Cart").document(cartIDAdded), 1, new Timestamp(new Date()) );

                                                addBookSelected(bookSelected).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if(task.isSuccessful()){
                                                            Log.d(TAG, "Added new BookSelected with ID: " + task.getResult().getId());
                                                            Log.d(TAG, "Thêm sách vào Cart thành công!");
                                                        } else {
                                                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                            }
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                    }
                }
            });
    }

    // Add new Cart doc autogenerate ID with converter
    private Task<DocumentReference> addCart (Cart cart){
        Map<String, Object> cartAdd = new HashMap<>();
        cartAdd.put("customerID", cart.getCustomerID());
        cartAdd.put("createdAt", cart.getCreatedAt());

        // Add new Cart for Customer
        return db.collection("Cart").add(cartAdd);
    }

    // Add new BookSelected doc autogenerate ID with converter
    private Task<DocumentReference> addBookSelected (BookSelected bookSelected){
        Map<String, Object> bookSelectedAdd = new HashMap<>();
        bookSelectedAdd.put("bookID", bookSelected.getBookID());
        bookSelectedAdd.put("cartID",  bookSelected.getCartID());
        bookSelectedAdd.put("quantity", bookSelected.getQuantity());
        bookSelectedAdd.put("createdAt", bookSelected.getCreatedAt());

        // Add new BookSelected has bookID and cartID
        return db.collection("BookSelected").add(bookSelectedAdd);
    }

    // Add rating doc autogenerate ID with converter
    private void addComment (Comment comment) {
        String bookID = ((DocumentReference)comment.getBookID()).getId();
        String customerID = ((DocumentReference)comment.getCustomerID()).getId();
        db.collection("Comment")
                .whereEqualTo("bookID", db.collection("Book").document(bookID))
                .whereEqualTo("customerID", db.collection("Customer").document(customerID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot commentDocs = task.getResult();
                            if(commentDocs.getDocuments().size() <= 0) {
                                Map<String, Object> commentAdd = new HashMap<>();
                                commentAdd.put("bookID", comment.getBookID());
                                commentAdd.put("customerID", comment.getCustomerID());
                                commentAdd.put("rate", comment.getRate());
                                commentAdd.put("createdAt", comment.getCreatedAt());

                                db.collection("Comment")
                                        .add(commentAdd)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){
                                                    Log.d(TAG, "Thêm đánh giá thành công! ID: " + task.getResult().getId());
                                                } else {
                                                    Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                }
                                            }
                                        });
                            } else {
                                Log.d(TAG, "Customer đã đánh giá cho cuốn sách này");
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    // Update totalRating, totalRatingStar and avgRated for Book item has bookID
    private void updateBookRated (String bookID, int ratedStar) {
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference bookDocRef = db.collection("Book").document(bookID);
                DocumentSnapshot bookSnapshot = transaction.get(bookDocRef);

                long prevTotalRating = (bookSnapshot.getLong("totalRating") != null) ? bookSnapshot.getLong("totalRating") : 0;
                long prevTotalRatingStar = (bookSnapshot.getLong("totalRatingStar") != null) ? bookSnapshot.getLong("totalRatingStar") : 0;
                long newTotalRating = prevTotalRating + 1;
                long newTotalRatingStar = prevTotalRatingStar + ratedStar;
                transaction.update(bookDocRef, "totalRating", newTotalRating, "totalRatingStar", newTotalRatingStar, "avgRated", (double)newTotalRatingStar / newTotalRating );
                Log.d(TAG, "Updated Rated for Book with BookID: " + bookID);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction update book rating successfully committed!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction update book rating failed: ", e);
            }
        });
    }

    // Update Comment and update totalRatingStar, avgRated of Book
    private void updateComment (String commentID, int newRatedStar) {
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference commentDocRef = db.collection("Comment").document(commentID);
                DocumentSnapshot commentSnapshot = transaction.get(commentDocRef);

                DocumentReference bookDocRef = (DocumentReference)commentSnapshot.getData().get("bookID");
                DocumentSnapshot bookSnapshot = transaction.get(bookDocRef);

                long prevTotalRating = (bookSnapshot.getLong("totalRating") != null) ? bookSnapshot.getLong("totalRating") : 0;
                long prevTotalRatingStar = (bookSnapshot.getLong("totalRatingStar") != null) ? bookSnapshot.getLong("totalRatingStar") : 0;
                long prevRatedStar = (commentSnapshot.getLong("rate") != null) ? commentSnapshot.getLong("rate") : 0;
                long newTotalRatingStar = prevTotalRatingStar + (newRatedStar - prevRatedStar);

                // Update new rate for comment
                transaction.update(commentDocRef, "rate", newRatedStar);
                Log.d(TAG, "Updated Rate for Comment with CommentID: " + commentDocRef.getId());

                // Update Rated in Book
                transaction.update(bookDocRef, "totalRatingStar", newTotalRatingStar, "avgRated", (double)newTotalRatingStar / prevTotalRating );
                Log.d(TAG, "Updated Rated for Book with BookID: " + bookDocRef.getId());
                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction update comment successfully committed!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction update comment failed: ", e);
            }
        });
    }

    // Get Comment by bookID and CustomerID
    private void getCommentedInBook (String bookID, String customerID) {
        db.collection("Comment")
                .whereEqualTo("bookID", db.collection("Book").document(bookID))
                .whereEqualTo("customerID", db.collection("Customer").document(customerID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot commentDocs = task.getResult();
                            if(commentDocs.getDocuments().size() > 0) {
                                Comment comment = commentDocs.getDocuments().get(0).toObject(Comment.class);
                                comment.setId(commentDocs.getDocuments().get(0).getId());
                                Log.d(TAG, "Thông tin đánh giá: " + comment);
                            } else {
                                Log.d(TAG, "Không tìm thấy thông tin đánh giá sách!");
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    // ====================================> Rounting 4: Cart <==========================================
    // Update quantity of BookSelected record with quanity = previousQuantity + 1;
    private void updateQuantityBookSelected (BookSelected bookSelected, int quantityIncrease) {
        int newQuantity = bookSelected.getQuantity() + quantityIncrease;
        if(newQuantity > 0){
            db.collection("BookSelected").document(bookSelected.getId())
                    .update("quantity", newQuantity)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "Cập nhật số lượng thành công from: " + bookSelected.getQuantity() +  " to: " + newQuantity);
                            } else {
                                Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                            }
                        }
                    });
        } else{
            deleteBookSelectedByID(bookSelected.getId());
        }
    }

    // Delete BookSelected by ID
    private void deleteBookSelectedByID (String bookSelectedID) {
        db.collection("BookSelected").document(bookSelectedID)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Deleted successfully BookSelected with ID: " + bookSelectedID);
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    // Get all BookSelected with CustomerID with converter
    private void getAllBookSelectedByCustomerID (String customerID) {
        db.collection("Cart")
                .whereEqualTo("customerID", db.collection("Customer").document(customerID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot cartDocs = task.getResult();
                            if(cartDocs.getDocuments().size() > 0) {
                                Cart cart = cartDocs.getDocuments().get(0).toObject(Cart.class);
                                cart.setId(cartDocs.getDocuments().get(0).getId());
                                Log.d(TAG, "Customer has cart withID: " + cart.getId());

                                db.collection("BookSelected")
                                        .whereEqualTo("cartID", db.collection("Cart").document(cart.getId()))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    QuerySnapshot bookSelectedDocs = task.getResult();
                                                    List<BookSelected> bookSelectedList = new ArrayList<>();

                                                    for (int i = 0; i < bookSelectedDocs.getDocuments().size(); i++) {
                                                        final int index = i;
                                                        BookSelected bookSelected = bookSelectedDocs.getDocuments().get(index).toObject(BookSelected.class);
                                                        bookSelected.setId(bookSelectedDocs.getDocuments().get(index).getId());
                                                        ((DocumentReference)bookSelected.getBookID())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if(task.isSuccessful()){
                                                                            DocumentSnapshot doc = task.getResult();
                                                                            if(doc.exists()){
                                                                                Book book = doc.toObject(Book.class);
                                                                                book.setId(doc.getId());
                                                                                bookSelected.setBookID(book);

                                                                                bookSelectedList.add(bookSelected);
//                                                                                Log.d(TAG, "BookSelected: " + bookSelected.toString());
                                                                            } else {
                                                                                Log.d(TAG, "Không tìm thấy thông tin sách ID: " + ((DocumentReference)bookSelected.getBookID()).getId());
                                                                            }
                                                                        } else {
                                                                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                                        }
                                                                        // Return data if end for loop
                                                                        if(index == bookSelectedDocs.getDocuments().size() - 1) {
                                                                            logListData(bookSelectedList);
//                                                                            ( (Button)findViewById(R.id.btnTestFirestore)).setText(bookSelectedList.size() + "xx");

//                                                                            double totalAmount = 0;
//                                                                            for (int i = 0; i < bookSelectedList.size(); i++) {
//                                                                                totalAmount += bookSelectedList.get(i).getQuantity() * ((Book)bookSelectedList.get(i).getBookID()).getPrice() * (1 - (((Book)bookSelectedList.get(i).getBookID()).getDiscount() / 100));
//                                                                            }
//
//                                                                            checkoutCart(bookSelectedList, new Order(db.collection("Customer").document("AnBBxrKJHzceljqhhTtr"), db.collection("OrderStatus").document("cSqT4qJKoZjMdvdVNSvi"), new Timestamp(new Date()), totalAmount, "King Wisdom", "0337220922", "Hoa Lac City"));
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                }
                                            }
                                        });
                            } else {
                                Log.d(TAG, "Không tìm thấy thông tin Cart của Customer!");
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    // ====================================> End Rounting 4: Cart <==========================================


    // ====================================> Rounting 5:  Book Categories <==========================================
    // Get all Book with CategoryID with converter
    private void getAllBookByCategoryID (String categoryID) {
        db.collection("Book")
                .whereEqualTo("1ID", db.collection("Category").document(categoryID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot docs = task.getResult();
                            if(docs.getDocuments().size() > 0) {
                                List<Book> bookList = new ArrayList<>();
                                for (int i = 0; i < docs.getDocuments().size(); i++) {
                                    Book book = docs.getDocuments().get(i).toObject(Book.class);
                                    book.setId(docs.getDocuments().get(i).getId());
                                    bookList.add(book);
                                }
                                logListData(bookList);
                            } else {
                                Log.d(TAG, "Không tìm thấy thông tin sách tương ứng");
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    private void getAllCategory() {
        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot docs = task.getResult();
                            if(docs.getDocuments().size() > 0) {
                                List<Category> categoryList = new ArrayList<>();
                                for (int i = 0; i < docs.getDocuments().size(); i++) {
                                    Category category = docs.getDocuments().get(i).toObject(Category.class);
                                    category.setId(docs.getDocuments().get(i).getId());
                                    categoryList.add(category);
                                }
                                logListData(categoryList);
                            } else {
                                Log.d(TAG, "Không tìm thấy danh mục sách tương ứng");
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());

                        }
                    }
                });
    }


    // ====================================> End Rounting 5:  Book Categories <==========================================


    // ====================================> Rounting 7:  Login <==========================================
    // Login Account with username + password and return Customer Info
    private void getCustomerInfoByLogin (String username, String password) {
        db.collection("Account")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot accountDocs = task.getResult();
                            if(accountDocs.getDocuments().size() > 0) {
                                Account account = accountDocs.getDocuments().get(0).toObject(Account.class);
                                if(account.getStatus() == false) {
                                    Log.d(TAG, "Tài khoản đang bị khóa, vui lòng liên hệ với Admin để tiếp tục truy cập");
                                } else {
                                    account.setId(accountDocs.getDocuments().get(0).getId());
                                    Log.d(TAG, account.toString());
                                    db.collection("Customer")
                                            .whereEqualTo("accountID", db.collection("Account").document(account.getId()))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        QuerySnapshot documents = task.getResult();
                                                        if (documents.getDocuments().size() > 0) {
                                                            Customer customer = documents.getDocuments().get(0).toObject(Customer.class);
                                                            customer.setId(documents.getDocuments().get(0).getId());
                                                            customer.setAccountID(account);

                                                            Log.d(TAG, "Đăng nhập thành công!");
                                                            Log.d(TAG, customer.toString());
                                                            Log.d(TAG, "Ussername: " + ((Account)customer.getAccountID()).getUsername());
                                                        } else {
                                                            Log.d(TAG, "Không tìm thấy thông tin Customer!");
                                                        }
                                                    } else {
                                                        Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.d(TAG, "Tên tài khoản hoặc mật khẩu sai!");
                            }
                        } else {
                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    // ====================================> End Rounting 7:  End Login <==========================================


    // ====================================> Rounting 8:  Sign Up <==========================================

    // Add new Account with converter
    private void addAccountAndCustomerInfo (Account account, Customer customer) {
        db.collection("Account").whereEqualTo("username", account.getUsername())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Check username existed or not
                        if (queryDocumentSnapshots.getDocuments().size() <= 0){
                            Map<String, Object> accountAdd = new HashMap<>();
                            accountAdd.put("username", account.getUsername());
                            accountAdd.put("password", account.getPassword());
                            accountAdd.put("status", true);
                            accountAdd.put("createdAt", new Timestamp(new Date()));

                            // Add new Account to Firestore
                            db.collection("Account")
                                    .add(accountAdd)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Account Added: " + documentReference.getId());
                                            Map<String, Object> customerAdd = new HashMap<>();
                                            customerAdd.put("accountID", db.collection("Account").document(documentReference.getId()));
                                            customerAdd.put("name", customer.getName());
                                            customerAdd.put("address", customer.getAddress());
                                            customerAdd.put("mail", customer.getMail());
                                            customerAdd.put("phone", customer.getPhone());
                                            customerAdd.put("createdAt", new Timestamp(new Date()));

                                            db.collection("Customer")
                                                    .add(customerAdd)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference cusDocumentReference) {
                                                            Log.d(TAG, "Customer Added: " + cusDocumentReference.getId());
                                                        }
                                                    });
                                        }
                                    });

                        } else {
                            Log.d(TAG, "Tài khoản đã tồn tại!");
                        }
                    }
                });
    }


    // ====================================> End Rounting 8: Sign Up <==========================================

    // ====================================> Rounting 9: Checkout cart <==========================================
    // Checkout all book in cart and do some logic
    private void checkoutCart (List<BookSelected> bookSelectedList, Order order) {
        Task t1 = db.collection("Book").get();
        Task t2 = db.collection("Stock").get();

        // Check book in stock is available or not to checkout,
        Task combinedTask = Tasks.whenAllSuccess(t1, t2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> list) {
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
                    Log.d(TAG, "Thanh toán thành công.");
                } else {
                    Log.d(TAG, "Số lượng trong kho không đủ.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Có lỗi khi lấy thông tin sách.");
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
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            DocumentReference orderDoc = task.getResult();
                            Log.d(TAG, "Added Order with ID: " + orderDoc.getId());

                            // Thêm nhiều orderitem mới
                            for (int i = 0; i < bookSelectedList.size(); i++) {
                                BookSelected item = bookSelectedList.get(i);
                                OrderItem orderItem = new OrderItem(db.collection("Book").document(((Book)item.getBookID()).getId()), db.collection("Order").document(orderDoc.getId()), item.getQuantity(), ((Book)item.getBookID()).getPrice(), ((Book)item.getBookID()).getDiscount());
                                addOrderItem(orderItem);
                            }
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
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            DocumentReference orderItemDoc = task.getResult();
                            Log.d(TAG, "Added OrderItem with ID: " + orderItemDoc.getId());
                        }
                    }
                });
    }

    // Update totalBookSelled with each book checkout and quantity in Stock
    private void updateQuantityBook (List<BookSelected> bookSelectedList) {
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
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
                    Log.d(TAG, "Updated Book ID " + item.getId());

                    transaction.update(db.collection("Stock").document(((Stock)item.getStockID()).getId()), "quantity", ((Stock)item.getStockID()).getQuantity(), "updatedDate", new Timestamp(new Date()));
                    Log.d(TAG, "Updated Stock ID " + ((Stock)item.getStockID()).getId());
                }

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction update totalBookSelled successfully committed!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction update totalBookSelled failed: ", e);
            }
        });
    }

//    private void getFirstBookUnAvailabeToSell (List<BookSelected> bookSelectedList) {
//        final boolean[] needCheckNextItem = {true};
//        for (int i = 0; i < bookSelectedList.size(); i++) {
//            if(needCheckNextItem[0]){
//                BookSelected item = bookSelectedList.get(i);
//                String bookId = ((Book) item.getBookID()).getId();
//                DocumentReference bookDoc = db.collection("Book").document(bookId);
//                bookDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if(task.getResult().exists()){
//                                Book book = task.getResult().toObject(Book.class);
//                                book.setId(task.getResult().getId());
//                                DocumentReference stockDocRef = (DocumentReference)book.getStockID();
//                                if(stockDocRef != null){
//                                    stockDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                            if(task.isSuccessful()){
//                                                if(task.getResult().exists()) {
//                                                    Stock stock = task.getResult().toObject(Stock.class);
//                                                    stock.setId(task.getResult().getId());
//                                                    int stockQuantity = stock.getQuantity();
//                                                    if(item.getQuantity() > stockQuantity){
//                                                        Log.d(TAG, "Số lượng sách ID: " + book.getId() + " trong kho không đủ.");
//                                                        needCheckNextItem[0] = false;
//                                                    } else {
//                                                        Log.d(TAG, "Số lượng trong kho thỏa mãn!");
//                                                    }
//                                                } else {
//                                                    Log.d(TAG, "Không tìm thấy thông tin kho!");
//                                                    needCheckNextItem[0] = false;
//                                                }
//                                            } else {
//                                                Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
//                                                needCheckNextItem[0] = false;
//                                            }
//                                        }
//                                    });
//                                } else {
//                                    Log.d(TAG, "Không tồn tại sách trong kho!");
//                                    needCheckNextItem[0] = false;
//                                }
//                            } else {
//                                Log.d(TAG, "Không tìm thấy thông tin sách!");
//                                needCheckNextItem[0] = false;
//                            }
//                        } else {
//                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
//                            needCheckNextItem[0] = false;
//                        }
//                    }
//                });
//            } else break;
//        }
//    }




    // ====================================> Rounting 10: Change Password <==========================================

    // Update password of account has accountID
    private void updatePassword (String accountID, String oldPassword, String newPassword) {
        DocumentReference documentReference = db.collection("Account").document(accountID);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Account account = documentSnapshot.toObject(Account.class);
                        if (account.getPassword().equals(oldPassword)){
                            documentReference.update("password", newPassword)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "Cập nhật mật khẩu thành công!");
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Sai mật khẩu!");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Cập nhật mật khẩu thất bại!");
                    }
                });
    }

    // ====================================> End Rounting 10: Change Password <==========================================



}