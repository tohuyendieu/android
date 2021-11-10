package fptu.ninhtbm.thebookshop.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.Constants;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.Category;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.account.AccountActivity;
import fptu.ninhtbm.thebookshop.ui.cart.CartActivity;
import fptu.ninhtbm.thebookshop.ui.home.adapter.BookRecyclerAdapter;
import fptu.ninhtbm.thebookshop.ui.home.adapter.SliderBannerAdapter;
import fptu.ninhtbm.thebookshop.ui.home.presenter.IMainPresenter;
import fptu.ninhtbm.thebookshop.ui.home.presenter.MainPresenter;
import fptu.ninhtbm.thebookshop.ui.listbook.ListBookActivity;
import fptu.ninhtbm.thebookshop.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private static final int NUMBER_BOOK_IN_PREVIEW = 10;

    private EditText mEdtSearch;
    private ImageButton mBtnMenu;
    private ImageButton mBtnCart;
    private ImageButton mBtnProfile;
    private TextView mCartBadge;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private SliderBannerAdapter mSliderBannerAdapter;
    private SliderView mSliderView;
    private List<String> mImageList;
    private RecyclerView mBestSaleBookRecyclerView;
    private RecyclerView mNewBookRecyclerView;
    private RecyclerView mTopRateBookRecyclerView;
    private RecyclerView mSaleOffBookRecyclerView;
    private BookRecyclerAdapter mBestSaleBookAdapter;
    private BookRecyclerAdapter mNewBookAdapter;
    private BookRecyclerAdapter mTopRateBookAdapter;
    private BookRecyclerAdapter mSaleOffBookAdapter;
    private List<Book> mBookBestSale;
    private List<Book> mBookTopNew;
    private List<Book> mBookTopRate;
    private List<Book> mBookSaleOff;
    private List<Category> mCategoryList;

    private long pressedTime;

    private IMainPresenter mPresenter;
    private Customer mCustomer;

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

        mEdtSearch = findViewById(R.id.edt_search);
        mBtnMenu = findViewById(R.id.btn_menu);
        mBtnCart = findViewById(R.id.btn_cart);
        mBtnProfile = findViewById(R.id.btn_profile);
        mCartBadge = findViewById(R.id.cart_badge);
        mSliderView = findViewById(R.id.slider_banner);
        mBestSaleBookRecyclerView = findViewById(R.id.rc_book_best_sale);
        mNewBookRecyclerView = findViewById(R.id.rc_book_top_new);
        mTopRateBookRecyclerView = findViewById(R.id.rc_book_top_rate);
        mSaleOffBookRecyclerView = findViewById(R.id.rc_book_top_sale_off);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);

        mPresenter = new MainPresenter(this);

        setUpAutoSlidingBanner();

        mBookBestSale = new ArrayList<>();
        mBookTopNew = new ArrayList<>();
        mBookTopRate = new ArrayList<>();
        mBookSaleOff = new ArrayList<>();

        mBestSaleBookAdapter = new BookRecyclerAdapter(this, mBookBestSale);
        mBestSaleBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mBestSaleBookRecyclerView.setAdapter(mBestSaleBookAdapter);

        mNewBookAdapter = new BookRecyclerAdapter(this, mBookTopNew);
        mNewBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mNewBookRecyclerView.setAdapter(mNewBookAdapter);

        mTopRateBookAdapter = new BookRecyclerAdapter(this, mBookTopRate);
        mTopRateBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTopRateBookRecyclerView.setAdapter(mTopRateBookAdapter);

        mSaleOffBookAdapter = new BookRecyclerAdapter(this, mBookSaleOff);
        mSaleOffBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSaleOffBookRecyclerView.setAdapter(mSaleOffBookAdapter);

        mCategoryList = new ArrayList<>();
        mCustomer = new SharePreferencesUtils(getApplicationContext()).getAccountCustomer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCustomer = new SharePreferencesUtils(getApplicationContext()).getAccountCustomer();
        setUpNavigationDrawer();
    }

    private void setUpAutoSlidingBanner() {
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
    }

    @SuppressLint("NonConstantResourceId")
    private void setListener() {
        MenuItem itemProfile = mNavigationView.getMenu().findItem(R.id.item_profile);
        MenuItem itemCart = mNavigationView.getMenu().findItem(R.id.item_cart);
        MenuItem itemLogout = mNavigationView.getMenu().findItem(R.id.item_logout);
        View headerNavigationView = mNavigationView.getHeaderView(0);
        MaterialButton btnLogin = headerNavigationView.findViewById(R.id.btn_login_nav_bar);
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        itemProfile.setOnMenuItemClickListener(menuItem -> {
            Intent intent;
            if (mCustomer != null) {
                intent = new Intent(MainActivity.this, AccountActivity.class);
            } else {
                intent = new Intent(MainActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            return true;
        });
        itemCart.setOnMenuItemClickListener(menuItem -> {
            Intent intent;
            if (mCustomer != null) {
                intent = new Intent(MainActivity.this, CartActivity.class);
            } else {
                intent = new Intent(MainActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            return true;
        });
        itemLogout.setOnMenuItemClickListener(menuItem -> {
            logout();
            return true;
        });

        mBtnMenu.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        mNavigationView.setNavigationItemSelectedListener(MenuItem -> {
            for (Category c : mCategoryList) {
                if (c.getTitle().contentEquals(MenuItem.getTitle())) {
                    Intent intent = new Intent(MainActivity.this, ListBookActivity.class);
                    intent.putExtra(ListBookActivity.CATEGORY_KEY, c);
                    startActivity(intent);
                }
            }

            MenuItem.setChecked(false);
            mDrawerLayout.closeDrawers();
            return true;
        });
        mBtnProfile.setOnClickListener(v -> {
            Intent intent;
            if (mCustomer != null) {
                intent = new Intent(this, AccountActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
        });
        mBtnCart.setOnClickListener(v -> {
            Intent intent;
            if (mCustomer != null) {
                intent = new Intent(this, CartActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
        });
        mEdtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = mEdtSearch.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, ListBookActivity.class);
                intent.putExtra(ListBookActivity.SEARCH_KEYWORD, keyword);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void logout() {
        SharePreferencesUtils sharePref = new SharePreferencesUtils(this);
        sharePref.removeAccountCustomer();
        mCustomer = null;
        setUpNavigationDrawer();
    }

    private void setUpNavigationDrawer() {
        View headerNavigationView = mNavigationView.getHeaderView(0);
        TextView labelLogin = headerNavigationView.findViewById(R.id.label_login_nav_bar);
        MaterialButton btnLogin = headerNavigationView.findViewById(R.id.btn_login_nav_bar);
        TextView textGreeting = headerNavigationView.findViewById(R.id.text_greeting);
        MenuItem itemLogout = mNavigationView.getMenu().findItem(R.id.item_logout);
        if (mCustomer != null) {
            labelLogin.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            textGreeting.setText(getString(R.string.txt_string_greeting, mCustomer.getName()));
            textGreeting.setVisibility(View.VISIBLE);
            itemLogout.setVisible(true);
        } else {
            labelLogin.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            textGreeting.setVisibility(View.GONE);
            itemLogout.setVisible(false);
        }
        if (mCustomer != null) {
            WidgetUtils.getNumberItemInCart(mCustomer.getId(), number -> {
                if (number > 0) {
                    mCartBadge.setText(String.valueOf(number));
                    mCartBadge.setVisibility(View.VISIBLE);
                } else {
                    mCartBadge.setVisibility(View.GONE);
                }
            });
        } else {
            mCartBadge.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onLoadData() {
        mCustomer = new SharePreferencesUtils(getApplicationContext()).getAccountCustomer();
        setUpNavigationDrawer();
        mPresenter.loadAllCategories();
        mPresenter.loadTopBookByField(NUMBER_BOOK_IN_PREVIEW, Constants.TYPE_SORT_BEST_SALE);
        mPresenter.loadTopBookByField(NUMBER_BOOK_IN_PREVIEW, Constants.TYPE_SORT_NEW);
        mPresenter.loadTopBookByField(NUMBER_BOOK_IN_PREVIEW, Constants.TYPE_SORT_RATE);
        mPresenter.loadTopBookByField(NUMBER_BOOK_IN_PREVIEW, Constants.TYPE_SORT_SALE_OFF);

        mImageList.add("https://images-production.bookshop.org/spree/promo_banner_slides/desktop_images/158/original/Belonging_TH_2048x600_%281%29.jpg");
        mImageList.add("https://images-production.bookshop.org/spree/promo_banner_slides/desktop_images/157/original/Oluo-Mediocre-BOOKSHOP-2048x600-rev1_%281%29.jpg");
        mSliderBannerAdapter.notifyDataSetChanged();
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

    @Override
    public void popSnackbarNotification(int messageResId) {
        WidgetUtils.showSnackbar(findViewById(R.id.root_layout), messageResId);
    }

    @Override
    public void loadCategories(List<Category> categoryList) {
        mCategoryList.clear();
        mCategoryList.addAll(categoryList);
        Menu m = mNavigationView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu(getString(R.string.txt_category));
        for (Category c : mCategoryList) {
            topChannelMenu.add(c.getTitle());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void loadTopBook(String sortType, List<Book> bookList) {
        switch (sortType) {
            case Constants.TYPE_SORT_BEST_SALE:
                mBookBestSale.clear();
                mBookBestSale.addAll(bookList);
                mBestSaleBookAdapter.notifyDataSetChanged();
                break;
            case Constants.TYPE_SORT_NEW:
                mBookTopNew.clear();
                mBookTopNew.addAll(bookList);
                mNewBookAdapter.notifyDataSetChanged();
                break;
            case Constants.TYPE_SORT_RATE:
                mBookTopRate.clear();
                mBookTopRate.addAll(bookList);
                mTopRateBookAdapter.notifyDataSetChanged();
                break;
            case Constants.TYPE_SORT_SALE_OFF:
                mBookSaleOff.clear();
                mBookSaleOff.addAll(bookList);
                mSaleOffBookAdapter.notifyDataSetChanged();
                break;
        }
    }
}