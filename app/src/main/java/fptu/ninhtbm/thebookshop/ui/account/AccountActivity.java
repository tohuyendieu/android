package fptu.ninhtbm.thebookshop.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.Validates;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Account;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.base.AboutAppActivity;
import fptu.ninhtbm.thebookshop.ui.changepassword.ChangePasswordActivity;
import fptu.ninhtbm.thebookshop.ui.login.LoginActivity;

public class AccountActivity extends AppCompatActivity {

    private Customer mCustomer;
    private ConstraintLayout mMainLayout;
    private final ActivityResultLauncher<Intent> changePasswordResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    WidgetUtils.showSnackbar(mMainLayout, R.string.text_noti_change_password_success);
                }
            });
    private TextView mTextTitle;
    private ImageButton mBtnBack;
    private ImageButton mBtnEdit;
    private TextView mTextUsername;
    private TextView mTextName;
    private TextView mTextPhone;
    private TextView mTextEmail;
    private TextView mTextAddress;
    private EditText mEdtName;
    private EditText mEdtPhone;
    private EditText mEdtEmail;
    private EditText mEdtAddress;
    private TextView mBtnChangePassword;
    private TextView mBtnLogout;
    private TextView mBtnHistory;
    private TextView mBtnAboutApp;
    private boolean isEditing;
    private LinearProgressIndicator mProgressBar;

    private FirebaseFirestore db;

    private SharePreferencesUtils sharePref;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        db = FirebaseFirestore.getInstance();
        sharePref = new SharePreferencesUtils(this);
        customer = sharePref.getAccountCustomer();

        if(customer != null) {
            initViews();
            setListener();
            initData(customer);
        } else {
            backToLogin();
        }
    }

    private void initViews() {
        mMainLayout = findViewById(R.id.main_layout);
        mTextTitle = findViewById(R.id.text_title);
        mBtnEdit = findViewById(R.id.btn_edit);
        mBtnBack = findViewById(R.id.btn_back);
        mTextUsername = findViewById(R.id.text_username);
        mTextName = findViewById(R.id.text_name);
        mTextPhone = findViewById(R.id.text_phone);
        mTextEmail = findViewById(R.id.text_email);
        mTextAddress = findViewById(R.id.text_address);
        mEdtName = findViewById(R.id.edt_name);
        mEdtPhone = findViewById(R.id.edt_phone);
        mEdtEmail = findViewById(R.id.edt_email);
        mEdtAddress = findViewById(R.id.edt_address);
        mBtnChangePassword = findViewById(R.id.btn_change_password);
        mBtnLogout = findViewById(R.id.btn_logout);
        mBtnHistory = findViewById(R.id.btn_history);
        mBtnAboutApp = findViewById(R.id.btn_about_app);
        mTextTitle.setText(getString(R.string.txt_account_info));
        mProgressBar = findViewById(R.id.progress_bar);
    }

    private void setListener() {
        mBtnBack.setOnClickListener(v -> {
            if(!isEditing){
                finish();
            } else {
                initScreenInfo();
            }
        });
        mBtnEdit.setOnClickListener(this::onEdit);
        mBtnLogout.setOnClickListener(this::onLogout);
        mBtnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            changePasswordResultLauncher.launch(intent);
        });
        mBtnHistory.setOnClickListener(v -> Toast.makeText(this, "Chức năng này hiện chưa có sẵn", Toast.LENGTH_SHORT).show());
        mBtnAboutApp.setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
        });

    }

    private void initData(Customer customer) {
        // Call firestore to get information
        mTextUsername.setText(((Account)customer.getAccountID()).getUsername());
        mTextName.setText(customer.getName());
        mTextPhone.setText(customer.getPhone());
        mTextEmail.setText(customer.getMail());
        mTextAddress.setText(customer.getAddress());
        mEdtName.setText(customer.getName());
        mEdtPhone.setText(customer.getPhone());
        mEdtEmail.setText(customer.getMail());
        mEdtAddress.setText(customer.getAddress());
    }

    private void initScreenInfo () {
        mTextTitle.setText(getString(R.string.txt_account_info));
        mBtnEdit.setImageResource(R.drawable.ic_round_edit_32);
        mEdtEmail.setVisibility(View.GONE);
        mEdtAddress.setVisibility(View.GONE);
        mEdtPhone.setVisibility(View.GONE);
        mEdtName.setVisibility(View.GONE);
        mTextEmail.setVisibility(View.VISIBLE);
        mTextAddress.setVisibility(View.VISIBLE);
        mTextPhone.setVisibility(View.VISIBLE);
        mTextName.setVisibility(View.VISIBLE);

        isEditing = false;
    }

    private void initScreenChangeInfo () {
        mTextTitle.setText(getString(R.string.txt_changing_account_info));
        mBtnEdit.setImageResource(R.drawable.ic_round_save_32);
        mEdtEmail.setVisibility(View.VISIBLE);
        mEdtAddress.setVisibility(View.VISIBLE);
        mEdtPhone.setVisibility(View.VISIBLE);
        mEdtName.setVisibility(View.VISIBLE);
        mTextEmail.setVisibility(View.GONE);
        mTextAddress.setVisibility(View.GONE);
        mTextPhone.setVisibility(View.GONE);
        mTextName.setVisibility(View.GONE);
        isEditing = true;
    }

    private void onEdit(View view) {
        if (isEditing) {
            // Call firestore save information, then pull new information
            // Change information in text view
            closeKeyboard();
            mEdtName.clearFocus();
            mEdtPhone.clearFocus();
            mEdtEmail.clearFocus();
            mEdtAddress.clearFocus();
            String name = mEdtName.getText().toString().trim();
            String phone = mEdtPhone.getText().toString().trim();
            String email = mEdtEmail.getText().toString().trim();
            String address = mEdtAddress.getText().toString().trim();

            if(isValidInput(name, phone, email, address)) {
                mProgressBar.setVisibility(View.VISIBLE);
                DocumentReference customerRef = db.collection("Customer").document(customer.getId());

                Map<String, Object> customerUpdate = new HashMap<>();
                customerUpdate.put("name", name);
                customerUpdate.put("address", address);
                customerUpdate.put("mail", email);
                customerUpdate.put("phone", phone);

                customerRef
                        .update(customerUpdate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                mProgressBar.setVisibility(View.INVISIBLE);
                                customer.setName(name);
                                customer.setAddress(address);
                                customer.setPhone(phone);
                                customer.setMail(email);
                                updateUILocal(customer);
                                initScreenInfo();
                                WidgetUtils.showSnackbar(mMainLayout, R.string.text_noti_save_account_info_success);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error updating document", e);
                                mProgressBar.setVisibility(View.INVISIBLE);
                                initScreenInfo();
                                WidgetUtils.showSnackbar(mMainLayout, R.string.text_noti_save_account_info_error);
                            }
                        });
            }
        } else {
            initScreenChangeInfo();
        }
    }

    private boolean isValidInput(String name, String phone, String email, String address) {
        if(name.equals("") ||
                email.equals("") ||
                phone.equals("") ||
                address.equals("")
        ) {
            WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_enter_fields);
            return false;
        }

        if(!Validates.validNumeric(phone) || !Validates.validatePhone(phone)){
            WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_phone_invalid);
            return false;
        }

        if(!Validates.validateEmail(email)) {
            WidgetUtils.showSnackbar(mMainLayout, R.string.txt_noti_email_invalid);
            return false;
        }

        return true;
    }

    private void updateUILocal(Customer customer) {
        mTextName.setText(customer.getName());
        mTextPhone.setText(customer.getPhone());
        mTextEmail.setText(customer.getMail());
        mTextAddress.setText(customer.getAddress());

        // Update info customer in local storage: SharedPreference
        sharePref.saveAccountCustomer(customer);
    }

    private void onLogout(View view) {
        SharePreferencesUtils sharePref = new SharePreferencesUtils(this);
        sharePref.removeAccountCustomer();
        backToLogin();
    }

    private void backToLogin() {
        Intent i = new Intent(AccountActivity.this, LoginActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}