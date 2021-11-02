package fptu.ninhtbm.thebookshop.ui.account;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fptu.ninhtbm.thebookshop.R;

public class AccountActivity extends AppCompatActivity {

    private ImageButton btnExit;
    private TextView btnViewDetailInformation;
    private TextView btnChangePassword;
    private TextView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initViews();
        setListener();
    }

    private void initViews() {
        btnExit = findViewById(R.id.btn_exit);
        btnViewDetailInformation = findViewById(R.id.btn_view_detail_information);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void setListener() {
        btnExit.setOnClickListener(v -> finish());
    }

}