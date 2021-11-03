package fptu.ninhtbm.thebookshop.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.ui.home.MainActivity;
import fptu.ninhtbm.thebookshop.ui.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mInputLayoutUsername;
    private TextInputEditText mInputTextUsername;
    private TextInputLayout mInputLayoutPassword;
    private TextInputEditText mInputTextPassword;
    private TextView mBtnForgotPassword;
    private MaterialButton mBtnLogin;
    private MaterialButton mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setListener();
    }


    private void initViews() {
        mInputLayoutUsername = findViewById(R.id.text_layout_username);
        mInputTextUsername = findViewById(R.id.edt_username);
        mInputLayoutPassword = findViewById(R.id.text_layout_old_password);
        mInputTextPassword = findViewById(R.id.edt_old_password);
        mBtnForgotPassword = findViewById(R.id.btn_forgot_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnSignUp = findViewById(R.id.btn_sign_up);
    }

    private void setListener() {
        mBtnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        mBtnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}