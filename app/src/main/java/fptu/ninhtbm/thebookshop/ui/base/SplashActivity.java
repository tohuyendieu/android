package fptu.ninhtbm.thebookshop.ui.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fptu.ninhtbm.thebookshop.library.Constants;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.home.MainActivity;
import fptu.ninhtbm.thebookshop.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharePreferencesUtils sharePreferencesUtils = new SharePreferencesUtils(getApplicationContext());
        Customer customer = sharePreferencesUtils.get(Constants.CUSTOMER_ACCOUNT_KEY, Customer.class);
        if (customer == null){
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();

    }
}