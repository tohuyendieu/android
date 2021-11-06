package fptu.ninhtbm.thebookshop.ui.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.model.Account;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.home.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private SharePreferencesUtils sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadCustomerInfoAndRedirect();
    }

    private void loadCustomerInfoAndRedirect () {
        db = FirebaseFirestore.getInstance();
        sharePref = new SharePreferencesUtils(this);
        Customer customer = sharePref.getAccountCustomer();

        if(customer != null) {
            Account account = (Account)customer.getAccountID();
            db.collection("Account")
                    .whereEqualTo("username", account.getUsername())
                    .whereEqualTo("password", account.getPassword())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                QuerySnapshot accountDocs = task.getResult();
                                if(accountDocs.getDocuments().size() > 0) {
                                    Account account = accountDocs.getDocuments().get(0).toObject(Account.class);
                                    if(account.getStatus() == false) {
//                                        Log.d(TAG, "Tài khoản đang bị khóa, vui lòng liên hệ với Admin để tiếp tục truy cập");
                                        removeInfoAndRedirect();
                                    } else {
                                        account.setId(accountDocs.getDocuments().get(0).getId());
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

//                                                                Log.d(TAG, "Đăng nhập thành công!");
                                                                sharePref.saveAccountCustomer(customer);
                                                                redirectToMain();
                                                            } else {
//                                                                Log.d(TAG, "Không tìm thấy thông tin Customer!");
                                                                removeInfoAndRedirect();
                                                            }
                                                        } else {
//                                                            Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                                            removeInfoAndRedirect();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
//                                    Log.d(TAG, "Tên tài khoản hoặc mật khẩu sai!");
                                    removeInfoAndRedirect();
                                }
                            } else {
//                                Log.d(TAG, "Có lỗi xảy ra: " + task.getException());
                                removeInfoAndRedirect();
                            }
                        }
                    });
        } else {
            removeInfoAndRedirect();
        }
    }

    private void removeInfoAndRedirect() {
        sharePref.removeAccountCustomer();
        redirectToMain();
    }

    private void redirectToMain() {
        // Redirect to Main Activity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

}