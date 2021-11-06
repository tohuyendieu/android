package fptu.ninhtbm.thebookshop.ui.signup.presenter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.Validates;
import fptu.ninhtbm.thebookshop.model.Account;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.base.BasePresenter;
import fptu.ninhtbm.thebookshop.ui.signup.ISignUpActivity;

public class SignUpPresenter extends BasePresenter<ISignUpActivity> implements ISignUpPresenter{

    public SignUpPresenter(ISignUpActivity activity) {
        super(activity);
    }

    @Override
    public void createAccountCustomer(String firstname, String lastname, String username, String email, String password, String repassword, String phone, String address) {
        if (isValidInput(firstname, lastname, username, email, password, repassword, phone, address)) {
            Account account = new Account(username, password);
            Customer customer = new Customer(firstname + "" + lastname, address, email, phone);

            mActivity.setProgressLoading(true);
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
//                                            Log.d(TAG, "Account Added: " + documentReference.getId());
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
//                                                            Log.d(TAG, "Customer Added: " + cusDocumentReference.getId());
                                                                mActivity.popSnackbarNotification(R.string.txt_noti_create_account_success);
                                                                mActivity.setProgressLoading(false);
                                                            }
                                                        });
                                            }
                                        });
                            } else {
//                            Log.d(TAG, "Tài khoản đã tồn tại!");
                                mActivity.popSnackbarNotification(R.string.txt_noti_account_existed);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mActivity.popSnackbarNotification(R.string.txt_noti_loading_failure);
                            mActivity.setProgressLoading(false);
                        }
                    });
        }
    }

    private boolean isValidInput(String firstname, String lastname, String username, String email, String password, String repassword, String phone, String address) {
        if(firstname.equals("") ||
                lastname.equals("") ||
                username.equals("") ||
                email.equals("") ||
                password.equals("") ||
                repassword.equals("") ||
                phone.equals("") ||
                address.equals("")
        ) {
            mActivity.popSnackbarNotification(R.string.txt_noti_enter_fields);
            return false;
        }

        if(username.length() < 6) {
            mActivity.popSnackbarNotification(R.string.txt_noti_username_invalid);
            return false;
        }

        if(!Validates.validateEmail(email)) {
            mActivity.popSnackbarNotification(R.string.txt_noti_email_invalid);
            return false;
        }

        if(password.length() < 6) {
            mActivity.popSnackbarNotification(R.string.txt_noti_password_invalid);
            return false;
        }

        if(!password.equals(repassword)){
            mActivity.popSnackbarNotification(R.string.txt_noti_repass_invalid);
            return false;
        }

        if(!Validates.validNumeric(phone) || !Validates.validatePhone(phone)){
            mActivity.popSnackbarNotification(R.string.txt_noti_phone_invalid);
            return false;
        }

        return true;
    }

}
