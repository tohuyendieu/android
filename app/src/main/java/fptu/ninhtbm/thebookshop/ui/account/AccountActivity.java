package fptu.ninhtbm.thebookshop.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.ui.changepassword.ChangePasswordActivity;
import fptu.ninhtbm.thebookshop.ui.login.LoginActivity;

public class AccountActivity extends AppCompatActivity {

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
    private boolean isEditing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initViews();
        setListener();
        initData();
    }

    private void initViews() {
        mMainLayout = findViewById(R.id.main_layout);
        mTextTitle = findViewById(R.id.text_title);
        mBtnEdit = findViewById(R.id.btn_edit);
        mBtnBack = findViewById(R.id.btn_back);
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
        mTextTitle.setText(getString(R.string.txt_account_info));

    }

    private void setListener() {
        mBtnBack.setOnClickListener(v -> finish());
        mBtnEdit.setOnClickListener(this::onEdit);
        mBtnLogout.setOnClickListener(this::onLogout);
        mBtnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            changePasswordResultLauncher.launch(intent);
        });

    }

    private void initData() {
        // todo: call firestore get information
    }

    private void onEdit(View view) {
        if (isEditing) {
            // todo: call firestore save information, then pull new information
            // todo: change information in text view

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
            WidgetUtils.showSnackbar(mMainLayout, R.string.text_noti_save_account_info_success);
        } else {
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
    }

    private void onLogout(View view) {
        SharePreferencesUtils sharePref = new SharePreferencesUtils(this);
        sharePref.removeAccountCustomer();
        Intent i = new Intent(AccountActivity.this, LoginActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}