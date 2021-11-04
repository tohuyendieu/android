package fptu.ninhtbm.thebookshop.ui.base;

import com.google.firebase.firestore.FirebaseFirestore;

import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;

public abstract class BasePresenter<I> {
    protected I mActivity;
    protected FirebaseFirestore db;

    public BasePresenter(I activity) {
        mActivity = activity;
        db = FirebaseFirestore.getInstance();
    }
}
