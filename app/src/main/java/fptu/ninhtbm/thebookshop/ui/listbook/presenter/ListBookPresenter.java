package fptu.ninhtbm.thebookshop.ui.listbook.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                            mActivity.popSnackbarNotification(R.string.txt_book_not_found_by_category);
                        }
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                    }
                });
    }

}
