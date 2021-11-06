package fptu.ninhtbm.thebookshop.ui.changepassword;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.Validates;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Account;
import fptu.ninhtbm.thebookshop.model.Customer;

public class ChangePasswordActivity extends AppCompatActivity {

    private ConstraintLayout mMainLayout;

    private MaterialButton mBtnConfirm;
    private ImageButton mBtnBack;
    private TextInputLayout mInputTextOldPassword;
    private TextInputLayout mInputTextNewPassword;
    private TextInputLayout mInputTextRePassword;

    private LinearProgressIndicator mProgressBar;

    private FirebaseFirestore db;
    private SharePreferencesUtils sharePref;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        db = FirebaseFirestore.getInstance();
        sharePref = new SharePreferencesUtils(this);
        customer = sharePref.getAccountCustomer();

        initViews();
        setListener();
    }

    private void initViews() {
        mMainLayout = findViewById(R.id.main_layout);
        mBtnBack = findViewById(R.id.layout_header).findViewById(R.id.btn_back);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mInputTextOldPassword = findViewById(R.id.text_layout_old_password);
        mInputTextNewPassword = findViewById(R.id.text_layout_new_password);
        mInputTextRePassword = findViewById(R.id.text_layout_repassword);
        ((TextView)findViewById(R.id.layout_header).findViewById(R.id.text_title)).setText("Đổi mật khẩu");
        mProgressBar = findViewById(R.id.progress_bar);
    }

    private void setListener() {
        mBtnBack.setOnClickListener(v -> finish());
        mBtnConfirm.setOnClickListener(this::onChangePassword);
    }

    private void onChangePassword(View view) {
        closeKeyboard();
        mInputTextOldPassword.clearFocus();
        mInputTextNewPassword.clearFocus();
        mInputTextRePassword.clearFocus();

        String oldPassword = mInputTextOldPassword.getEditText().getText().toString().trim();
        String newPassword = mInputTextNewPassword.getEditText().getText().toString().trim();
        String repassword = mInputTextRePassword.getEditText().getText().toString().trim();
        String accountID = ((Account)customer.getAccountID()).getId();

        if(isValidInput(oldPassword, newPassword, repassword)) {
            mProgressBar.setVisibility(View.VISIBLE);
            // Update password of account has accountID
            DocumentReference documentReference = db.collection("Account").document(accountID);
            documentReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Account account = documentSnapshot.toObject(Account.class);
                            account.setId(documentSnapshot.getId());
                            if(account != null) {
                                if (account.getPassword().equals(oldPassword)){
                                    documentReference.update("password", newPassword)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
    //                                                Log.d(TAG, "Cập nhật mật khẩu thành công!");
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    setResult(RESULT_OK);
                                                    finish();

                                                    account.setPassword(newPassword);
                                                    customer.setAccountID(account);
                                                    sharePref.saveAccountCustomer(customer);
                                                }
                                            });
                                } else {
    //                                Log.d(TAG, "Sai mật khẩu!");
                                    WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_password_error);
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_loading_failure);
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Log.d(TAG, "Cập nhật mật khẩu thất bại!");
                            WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_loading_failure);
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }

    }

    private boolean isValidInput(String oldPassword, String newPassword, String repassword) {
        if(oldPassword.equals("") ||
                newPassword.equals("") ||
                repassword.equals("")
        ) {
            WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_enter_fields);
            return false;
        }

        if(newPassword.length() < 6) {
            WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_password_invalid);
            return false;
        }

        if(!newPassword.equals(repassword)){
            WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_repass_invalid);
            return false;
        }

        return true;
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}