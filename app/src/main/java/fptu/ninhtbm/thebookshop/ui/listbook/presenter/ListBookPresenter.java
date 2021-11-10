package fptu.ninhtbm.thebookshop.ui.listbook.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.ui.base.BasePresenter;
import fptu.ninhtbm.thebookshop.ui.listbook.IListBookActivity;

public class ListBookPresenter extends BasePresenter<IListBookActivity> implements IListBookPresenter {
    public ListBookPresenter(IListBookActivity activity) {
        super(activity);
    }

    @Override
    public void loadBookByCategoryId(String categoryId) {
        db.collection("Book")
                .whereEqualTo("categoryID", db.collection("Category").document(categoryId))
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot docs = task.getResult();
                        if(docs.getDocuments().size() > 0) {
                            List<Book> bookList = new ArrayList<>();
                            for (int i = 0; i < docs.getDocuments().size(); i++) {
                                Book book = docs.getDocuments().get(i).toObject(Book.class);
                                book.setId(docs.getDocuments().get(i).getId());
                                bookList.add(book);
                            }
                            mActivity.loadBooks(bookList);
                        } else {
                            mActivity.loadBooks(null);
                        }
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    }
                });
    }

    @Override
    public void searchBooksByTitle(String keyword) {
        db.collection("Book")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Book> allBook = new ArrayList<>();
                            List<Book> searchBooks = new ArrayList<>();

                            if(task.getResult().getDocuments().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Book book = document.toObject(Book.class);
                                    book.setId(document.getId());
                                    allBook.add(book);
                                }
                                for (int i = 0; i < allBook.size(); i++) {
                                    if(allBook.get(i).getTitle().toLowerCase().contains(keyword.toLowerCase())){
                                        searchBooks.add(allBook.get(i));
                                    }
                                }
//                                Log.d(TAG, "Kết quả tìm kiếm với \""+ keyword + "\": ");
                                mActivity.loadBooks(searchBooks);
                            } else {
                                mActivity.loadBooks(searchBooks);
                            }
                        } else {
                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                        }
                    }
                });
    }

}
