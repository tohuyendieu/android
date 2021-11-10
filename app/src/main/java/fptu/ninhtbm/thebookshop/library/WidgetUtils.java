package fptu.ninhtbm.thebookshop.library;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Cart;

public class WidgetUtils {
    public static void showSnackbar(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

    public static Dialog getCustomDialogSetUp(Context context, int ResId) {
        Dialog dialog = new Dialog(context);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(ResId);
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        return dialog;
    }


    public static Dialog showDialogSuccessCheckedGreen(Context context, String message, View.OnClickListener action) {
        Dialog dialog = new Dialog(context);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_review_checked);
        TextView textMessage = dialog.findViewById(R.id.txt_message);
        textMessage.setText(message);
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.setOnShowListener(dialogInterface -> {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(2000); // Delay 2s and close dialog
                    action.onClick(null);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });
        return dialog;
    }

    public static void getNumberItemInCart(String customerId, CallbackAction action) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cart")
                .whereEqualTo("customerID", db.collection("Customer").document(customerId))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot cartDocs = task.getResult();
                            if (cartDocs.getDocuments().size() > 0) {
                                Cart cart = cartDocs.getDocuments().get(0).toObject(Cart.class);
                                cart.setId(cartDocs.getDocuments().get(0).getId());
//                                Log.d(TAG, "Customer has cart withID: " + cart.getId());

                                db.collection("BookSelected")
                                        .whereEqualTo("cartID", db.collection("Cart").document(cart.getId()))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot bookSelectedDocs = task.getResult();
                                                    List<BookSelected> bookSelectedList = new ArrayList<>();

                                                    for (int i = 0; i < bookSelectedDocs.getDocuments().size(); i++) {
                                                        final int index = i;
                                                        BookSelected bookSelected = bookSelectedDocs.getDocuments().get(index).toObject(BookSelected.class);
                                                        bookSelected.setId(bookSelectedDocs.getDocuments().get(index).getId());
                                                        ((DocumentReference) bookSelected.getBookID())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot doc = task.getResult();
                                                                            if (doc.exists()) {
                                                                                Book book = doc.toObject(Book.class);
                                                                                book.setId(doc.getId());
                                                                                bookSelected.setBookID(book);

                                                                                bookSelectedList.add(bookSelected);
//                                                                                Log.d(TAG, "BookSelected: " + bookSelected.toString());
                                                                            } else {
//                                                                                Log.d(TAG, "Không tìm thấy thông tin sách ID: " + ((DocumentReference)bookSelected.getBookID()).getId());
                                                                            }
                                                                        } else {
//                                                                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                                        }
                                                                        // Return data if end for loop
                                                                        if (index == bookSelectedDocs.getDocuments().size() - 1) {
                                                                            action.loadNumberItemCart(bookSelectedList.size());
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
//                                                    Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                }
                                            }
                                        });
                            } else {
//                                Log.d(TAG, "Không tìm thấy thông tin Cart của Customer!");
                            }
                        } else {
//                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                        }
                    }
                });
    }

    public interface CallbackAction {
        void loadNumberItemCart(int number);
    }
}
