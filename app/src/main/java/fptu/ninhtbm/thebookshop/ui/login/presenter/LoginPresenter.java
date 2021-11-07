package fptu.ninhtbm.thebookshop.ui.login.presenter;

import com.google.firebase.firestore.QuerySnapshot;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.model.Account;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.base.BasePresenter;
import fptu.ninhtbm.thebookshop.ui.login.ILoginActivity;
import fptu.ninhtbm.thebookshop.ui.login.LoginActivity;

public class LoginPresenter extends BasePresenter<ILoginActivity> implements ILoginPresenter {

    private final SharePreferencesUtils sharePreferencesUtils;

    public LoginPresenter(ILoginActivity mActivity) {
        super(mActivity);
        sharePreferencesUtils = new SharePreferencesUtils((LoginActivity) mActivity);

    }


    @Override
    public void authenticateAccount(String username, String password) {
        mActivity.setProgressLoading(true);
        db.collection("Account")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot accountDocs = task.getResult();
                        if (accountDocs.getDocuments().size() > 0) {
                            Account account = accountDocs.getDocuments().get(0).toObject(Account.class);
                            if (!account.getStatus()) {
                                mActivity.popSnackbarNotification(R.string.txt_noti_account_disabled_login);
                            } else {
                                account.setId(accountDocs.getDocuments().get(0).getId());
//                                    Log.d(TAG, account.toString());
                                db.collection("Customer")
                                        .whereEqualTo("accountID", db.collection("Account").document(account.getId()))
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                QuerySnapshot documents = task1.getResult();
                                                if (documents.getDocuments().size() > 0) {
                                                    Customer customer = documents.getDocuments().get(0).toObject(Customer.class);
                                                    customer.setId(documents.getDocuments().get(0).getId());
                                                    customer.setAccountID(account);
                                                    sharePreferencesUtils.saveAccountCustomer(customer);
//                                                    sharePreferencesUtils.put(Constants.CUSTOMER_ACCOUNT_KEY, Customer.class);
                                                    mActivity.returnLoginRequest();
//                                                    Log.d(TAG, "Đăng nhập thành công!");
//                                                    Log.d(TAG, customer.toString());
                                                } else {
//                                                        Log.d(TAG, .getString(R.string.txt_noti_account_login_not_found));
                                                    mActivity.popSnackbarNotification(R.string.txt_noti_account_login_not_found);
                                                }
                                            } else {
                                                mActivity.popSnackbarNotification(R.string.txt_noti_login_failure);
                                            }
                                        });
                            }
                        } else {
//                                Log.d(TAG, getString(R.string.txt_account_or_password_not_match));
                            mActivity.popSnackbarNotification(R.string.txt_account_or_password_not_match);
                        }
                    } else {
                        mActivity.popSnackbarNotification(R.string.txt_noti_login_failure);
                    }
                    mActivity.setProgressLoading(false);
                });
    }
}
