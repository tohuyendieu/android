package fptu.ninhtbm.thebookshop.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.ui.forgotpassword.ForgotPasswordActivity;
import fptu.ninhtbm.thebookshop.ui.home.MainActivity;
import fptu.ninhtbm.thebookshop.ui.login.presenter.ILoginPresenter;
import fptu.ninhtbm.thebookshop.ui.login.presenter.LoginPresenter;
import fptu.ninhtbm.thebookshop.ui.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements ILoginActivity {

    public static final String IS_REQUEST_FROM_BOOK_DETAIL = "IS_REQUEST_FROM_BOOK_DETAIL";

    private TextInputLayout mInputLayoutUsername;
    private TextInputLayout mInputLayoutPassword;
    private LinearProgressIndicator mProgressBar;
    private TextView mBtnForgotPassword;
    private MaterialButton mBtnLogin;
    private MaterialButton mBtnSignUp;

    private ILoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setListener();
    }


    private void initViews() {
        mProgressBar = findViewById(R.id.progress_bar);
        mInputLayoutUsername = findViewById(R.id.text_layout_username);
        mInputLayoutPassword = findViewById(R.id.text_layout_old_password);
        mBtnForgotPassword = findViewById(R.id.btn_forgot_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnSignUp = findViewById(R.id.btn_sign_up);
        mPresenter = new LoginPresenter(this);
    }

    private void setListener() {
        mBtnLogin.setOnClickListener(v -> {
            closeKeyboard();
            mInputLayoutUsername.clearFocus();
            mInputLayoutPassword.clearFocus();
            String username = mInputLayoutUsername.getEditText().getText().toString().trim();
            String password = mInputLayoutPassword.getEditText().getText().toString().trim();
            mPresenter.authenticateAccount(username, password);
        });
        mBtnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        mBtnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void popSnackbarNotification(int messageResId) {
        WidgetUtils.showSnackbar(findViewById(R.id.root_layout), messageResId);
    }

    @Override
    public void returnLoginRequest() {
        if(getIntent().getBooleanExtra(IS_REQUEST_FROM_BOOK_DETAIL, false)){
            setResult(Activity.RESULT_OK);
            finish();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setProgressLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}