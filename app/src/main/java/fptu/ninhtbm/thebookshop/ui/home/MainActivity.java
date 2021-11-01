package fptu.ninhtbm.thebookshop.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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

    @SuppressLint("NotifyDataSetChanged")
    private void onLoadData() {
        // Todo: fake number badge in cart
        badgeDrawable.setNumber(9);
        // Todo: fake banner image
        mImageList.add("https://www.geeksforgeeks.org/wp-content/uploads/gfg_200X200-1.png");
        mImageList.add("https://qphs.fs.quoracdn.net/main-qimg-8e203d34a6a56345f86f1a92570557ba.webp");
        mImageList.add("https://bizzbucket.co/wp-content/uploads/2020/08/Life-in-The-Metro-Blog-Title-22.png");
        mSliderBannerAdapter.notifyDataSetChanged();
        // todo: fake book data
        mBookList.add(new Book(1, "ngu", "https://images-us.bookshop.org/ingram/9781538730225.jpg"));
        mBookList.add(new Book(1, "ngu", "https://images-us.bookshop.org/ingram/9781538730225.jpg"));
        mBookList.add(new Book(1, "ngu", "https://images-us.bookshop.org/ingram/9781538730225.jpg"));
        mBestSaleBookAdapter.notifyDataSetChanged();
    }
}