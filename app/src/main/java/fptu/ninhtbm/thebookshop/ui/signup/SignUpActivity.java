package fptu.ninhtbm.thebookshop.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.ui.signup.presenter.ISignUpPresenter;
import fptu.ninhtbm.thebookshop.ui.signup.presenter.SignUpPresenter;

public class SignUpActivity extends AppCompatActivity implements ISignUpActivity{

    private LinearProgressIndicator mProgressBar;
    private TextInputLayout mInputLayoutFirstname;
    private TextInputLayout mInputLayoutLastname;
    private TextInputLayout mInputLayoutUsername;
    private TextInputLayout mInputLayoutEmail;
    private TextInputLayout mInputLayoutOldPassword;
    private TextInputLayout mInputLayoutRePassword;
    private TextInputLayout mInputLayoutPhone;
    private TextInputLayout mInputLayoutAddress;
    private CheckBox mCbConfirmService;
    private MaterialButton mBtnSignUp;
    private MaterialButton mBtnLogin;

    private ISignUpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        setListener();
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.progress_bar);
        mInputLayoutFirstname = findViewById(R.id.text_layout_firstname);
        mInputLayoutLastname = findViewById(R.id.text_layout_lastname);
        mInputLayoutUsername = findViewById(R.id.text_layout_username);
        mInputLayoutEmail = findViewById(R.id.text_layout_email);
        mInputLayoutOldPassword = findViewById(R.id.text_layout_old_password);
        mInputLayoutRePassword = findViewById(R.id.text_layout_repassword);
        mInputLayoutPhone = findViewById(R.id.text_layout_phone);
        mInputLayoutAddress = findViewById(R.id.text_layout_address);
        mCbConfirmService = findViewById(R.id.cb_confirm_service);
        mBtnSignUp = findViewById(R.id.btn_sign_up);
        mBtnLogin = findViewById(R.id.btn_login);

        mPresenter = new SignUpPresenter(this);
    }

    private void setListener() {
        mBtnSignUp.setOnClickListener(v -> {
            mInputLayoutFirstname.clearFocus();
            mInputLayoutLastname.clearFocus();
            mInputLayoutUsername.clearFocus();
            mInputLayoutEmail.clearFocus();
            mInputLayoutOldPassword.clearFocus();
            mInputLayoutRePassword.clearFocus();
            mInputLayoutPhone.clearFocus();
            mInputLayoutAddress.clearFocus();

            String firstname = mInputLayoutFirstname.getEditText().getText().toString().trim();
            String lastname = mInputLayoutLastname.getEditText().getText().toString().trim();
            String username = mInputLayoutUsername.getEditText().getText().toString().trim();
            String email = mInputLayoutEmail.getEditText().getText().toString().trim();
            String password = mInputLayoutOldPassword.getEditText().getText().toString().trim();
            String repassword = mInputLayoutRePassword.getEditText().getText().toString().trim();
            String phone = mInputLayoutPhone.getEditText().getText().toString().trim();
            String address = mInputLayoutAddress.getEditText().getText().toString().trim();

            mPresenter.createAccountCustomer(firstname, lastname, username, email, password, repassword, phone, address);
        });
        mCbConfirmService.setOnClickListener(v -> {
            boolean isConfirmService = mCbConfirmService.isChecked();
            mBtnSignUp.setEnabled(isConfirmService);
        });
        mBtnLogin.setOnClickListener(v -> {
            backToLogin();
        });
    }

    @Override
    public void popSnackbarNotification(int messageResId) {
        WidgetUtils.showSnackbar(findViewById(R.id.root_layout), messageResId);
    }

    @Override
    public void backToLogin() {
        finish();
    }

    @Override
    public void setProgressLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

}