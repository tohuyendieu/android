package fptu.ninhtbm.thebookshop.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.ui.account.AccountActivity;
import fptu.ninhtbm.thebookshop.ui.cart.CartActivity;
import fptu.ninhtbm.thebookshop.ui.home.adapter.BookRecyclerAdapter;
import fptu.ninhtbm.thebookshop.ui.home.adapter.SliderBannerAdapter;

public class MainActivity extends AppCompatActivity {

    private ImageButton mBtnMenu;
    private ImageButton mBtnCart;
    private ImageButton mBtnProfile;
    private BadgeDrawable badgeDrawable;

    private SliderBannerAdapter mSliderBannerAdapter;
    private SliderView mSliderView;
    private List<String> mImageList;
    private RecyclerView mBestSaleBookRecyclerView;
    private BookRecyclerAdapter mBestSaleBookAdapter;
    private List<Book> mBookList;

    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListener();
        onLoadData();
    }

    @SuppressLint({"UnsafeExperimentalUsageError", "ResourceAsColor"})
    private void initViews() {
        mBtnMenu = findViewById(R.id.btn_menu);
        mBtnCart = findViewById(R.id.btn_cart);
        mBtnProfile = findViewById(R.id.btn_profile);
        mSliderView = findViewById(R.id.slider_banner);
        mBestSaleBookRecyclerView = findViewById(R.id.rc_list_best_sale_book);

        badgeDrawable = BadgeDrawable.create(this);
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
        badgeDrawable.setBackgroundColor(R.color.important_button_color);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        BadgeUtils.attachBadgeDrawable(badgeDrawable, mBtnCart);

        mImageList = new ArrayList<>();
        mSliderBannerAdapter = new SliderBannerAdapter(this, mImageList);
        mSliderView.setSliderAdapter(mSliderBannerAdapter);
        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.

        mSliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is use to set
        // scroll time in seconds.
        mSliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        mSliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        mSliderView.startAutoCycle();

        mBookList = new ArrayList<>();
        mBestSaleBookAdapter = new BookRecyclerAdapter(this, mBookList);
        mBestSaleBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBestSaleBookRecyclerView.setAdapter(mBestSaleBookAdapter);
    }

    private void setListener() {
        mBtnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
        mBtnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onLoadData() {
        // Todo: fake number badge in cart
        badgeDrawable.setNumber(9);
        mImageList.add("https://images-production.bookshop.org/spree/promo_banner_slides/desktop_images/154/original/ABCH_Fall21_2048x600_Ad.jpg");
        mImageList.add("https://images-production.bookshop.org/spree/promo_banner_slides/desktop_images/156/original/The_Dawn_Of_Everything_-_Bookshop_-_2048x600.jpg");
        mImageList.add("https://images-production.bookshop.org/spree/promo_banner_slides/desktop_images/154/original/ABCH_Fall21_2048x600_Ad.jpg`");
        mSliderBannerAdapter.notifyDataSetChanged();
        // todo: fake book data
        Book b = new Book();
        b.setBookCoverImg("https://images-us.bookshop.org/ingram/9781538730225.jpg");
        mBookList.add(b);
        mBookList.add(b);
        mBookList.add(b);
        mBestSaleBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.text_double_back_exit), Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();

    }
}