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
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Account;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Category;
import fptu.ninhtbm.thebookshop.model.Comment;
import fptu.ninhtbm.thebookshop.model.Customer;
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
//        Toast.makeText(this, "work", Toast.LENGTH_SHORT).show();
//        testFirestore();

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

//        addBookSelected(new BookSelected(db.collection("Book").document("123"), db.collection("Cart").document("234"), 1, new Timestamp(new Date())));

//        getAllBookByCategoryID("zna5C9ZCKki5V8L97LZn");  // Rounting 5

//        addAccountAndCustomerInfo(new Account("King123", "123456"), new Customer("King Wisdom", "Hòa Lạc",  "kingwisdom.dev@gmail.com", "0337220922"));  // Rounting 8

//        getCustomerInfoByLogin("account13", "account13");  // Rounting 7

//        updatePassword("FeuU4tA9sjd5eCQkn8UF", "123456", "account12");  // Rounting 10

    }

    private void logListData(List list){
        Log.d(TAG, "\n==================== LOG LIST DATA WITH SIZE = " + list.size() + " ====================");
        for (int i = 0; i < list.size(); i++){
            Log.d(TAG, "Item " + i + " : " + list.get(i).toString());
        }
    }

    private void testFirestore() {
//        DocumentReference docRef = db.collection("Account").document("5jDFIM3oQMmfsQIWFBOa");
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                Account author = documentSnapshot.toObject(Account.class);
//                author.setId(documentSnapshot.getId());
//                Log.d("Firestore", author.toString());
//                Log.d("Firestore", String.valueOf(documentSnapshot.getData()));
////                Log.d("Firestore", documentSnapshot.getId());
////                Log.d("Firestore", author.getPassword());
////                Log.d("Firestore", author.getCreatedAt().toString());
////                Log.d("Firestore", author.isStatus() + "");
//            }
//        });


        DocumentReference docRef = db.collection("Account").document("5jDFIM3oQMmfsQIWFBOa");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firestore", "DocumentSnapshot data: " + document.getData());
                        Log.d("Firestore", "DocumentSnapshot data1: " + (document.toObject(Account.class)).toString());
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                }
            }
        });

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
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document", e);
                    }
                });
    }

    // Add new BookSelected doc autogenerate ID with converter
    private void addBookSelected (BookSelected bookSelected) {
        Map<String, Object> bookSelectedAdd = new HashMap<>();
        bookSelectedAdd.put("bookID", bookSelected.getBookID());
        bookSelectedAdd.put("cartID",  bookSelected.getCartID());
        bookSelectedAdd.put("quantity", bookSelected.getQuantity());
        bookSelectedAdd.put("createdAt", bookSelected.getCreatedAt());

        db.collection("BookSelected")
                .add(bookSelectedAdd)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Added new BookSelected with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding BookSelected", e);
                    }
                });
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


    // ====================================> Rounting 5:  Book Categories <==========================================
    // Get all Book with CategoryID with converter
    private void getAllBookByCategoryID (String categoryID) {
        db.collection("Book")
                .whereEqualTo("categoryID", db.collection("Category").document(categoryID))
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
                                if(account.isStatus() == false) {
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