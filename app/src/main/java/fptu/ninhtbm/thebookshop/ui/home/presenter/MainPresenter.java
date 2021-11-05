package fptu.ninhtbm.thebookshop.ui.home.presenter;

import android.util.Log;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Category;
import fptu.ninhtbm.thebookshop.ui.base.BasePresenter;
import fptu.ninhtbm.thebookshop.ui.home.IMainActivity;

public class MainPresenter extends BasePresenter<IMainActivity> implements IMainPresenter {
    public MainPresenter(IMainActivity activity) {
        super(activity);
    }

    @Override
    public void loadAllCategories() {
        db.collection("Category")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot docs = task.getResult();
                        if (docs.getDocuments().size() > 0) {
                            List<Category> categoryList = new ArrayList<>();
                            for (int i = 0; i < docs.getDocuments().size(); i++) {
                                Category category = docs.getDocuments().get(i).toObject(Category.class);
                                category.setId(docs.getDocuments().get(i).getId());
                                categoryList.add(category);
                            }
                            mActivity.loadCategories(categoryList);
                        } else {
                            mActivity.popSnackbarNotification(R.string.txt_load_caterory_error);
                        }
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);

                    }
                });
    }

    @Override
    public void loadTopBookByField(int topBook, String sortType) {

    }
}
