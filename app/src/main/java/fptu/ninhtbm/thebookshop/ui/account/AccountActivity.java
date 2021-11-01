package fptu.ninhtbm.thebookshop.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import fptu.ninhtbm.thebookshop.R;

public class AccountActivity extends AppCompatActivity {

//    private ImageButton btnBack;
//    private ImageButton btnOption;
//    private TextView textLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initViews();
        setListener();
    }
    private void initViews() {
//        btnBack = findViewById(R.id.btn_back);
//        btnOption = findViewById(R.id.btn_option);
//        btnOption.setVisibility(View.VISIBLE);
//        textLabel = findViewById(R.id.text_label);
//        textLabel.setText(getString(R.string.txt_account_info));
    }

    private void setListener() {
//        btnBack.setOnClickListener(v -> finish());
//        btnOption.setOnClickListener( v -> Toast.makeText(this, "Edit account", Toast.LENGTH_SHORT).show());
    }

}