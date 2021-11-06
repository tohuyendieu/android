package fptu.ninhtbm.thebookshop.ui.listbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fptu.ninhtbm.thebookshop.R;

public class ListBookActivity extends AppCompatActivity {
    public static String CATEGORY_KEY = "CATEGORY_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
    }
}