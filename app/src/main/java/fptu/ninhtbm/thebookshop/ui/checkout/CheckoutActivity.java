package fptu.ninhtbm.thebookshop.ui.checkout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fptu.ninhtbm.thebookshop.R;

public class CheckoutActivity extends AppCompatActivity {

    public static final String CART_KEY =  "CART_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }

}