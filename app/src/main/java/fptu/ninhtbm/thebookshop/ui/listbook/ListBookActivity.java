package fptu.ninhtbm.thebookshop.ui.listbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.Category;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.account.AccountActivity;
import fptu.ninhtbm.thebookshop.ui.cart.CartActivity;
import fptu.ninhtbm.thebookshop.ui.listbook.adapter.BookAdapter;
import fptu.ninhtbm.thebookshop.ui.listbook.presenter.IListBookPresenter;
import fptu.ninhtbm.thebookshop.ui.listbook.presenter.ListBookPresenter;
import fptu.ninhtbm.thebookshop.ui.login.LoginActivity;

public class ListBookActivity extends AppCompatActivity implements IListBookActivity {
    public static final String CATEGORY_KEY = "CATEGORY_KEY";

    private static final int ORDER_BY_RATE = 1;
    private static final int ORDER_BY_DATE = 2;
    private static final int ORDER_BY_SALE = 3;
    private static final int ORDER_BY_PRICE = 4;


    private ImageButton mBtnBack;
    private ImageButton mBtnCart;
    private ImageButton mBtnProfile;
    private TextView mBtnSortByRate;
    private TextView mBtnSortByDate;
    private TextView mBtnSortBySale;
    private TextView mBtnSortByPrice;
    private BadgeDrawable badgeDrawable;
    private RecyclerView mRecyclerViewBook;
    private BookAdapter mBookAdapter;
    private CircularProgressIndicator mProgressBar;
    private IListBookPresenter mPresenter;
    private Customer mCustomer;
    private List<Book> mBookList;
    private Category mCategory;

    private int currentOption;
    private boolean isLowPrice = true;
    private boolean isSortingRate;
    private boolean isSortingDate;
    private boolean isSortingSale;
    private boolean isSortingPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        initViews();
        setListener();
        initData();
    }

    @SuppressLint("ResourceAsColor")
    private void initViews() {
        mPresenter = new ListBookPresenter(this);

        mBtnBack = findViewById(R.id.btn_menu);
        mBtnBack.setImageResource(R.drawable.ic_round_arrow_back_40);
        mBtnCart = findViewById(R.id.btn_cart);
        mBtnProfile = findViewById(R.id.btn_profile);
        mBtnSortByRate = findViewById(R.id.order_rate);
        mBtnSortByDate = findViewById(R.id.order_date);
        mBtnSortBySale = findViewById(R.id.order_sale);
        mBtnSortByPrice = findViewById(R.id.order_price);
        mRecyclerViewBook = findViewById(R.id.rv_book);
        mProgressBar = findViewById(R.id.progress_bar);
        mBookList = new ArrayList<>();
        mBookAdapter = new BookAdapter(this, mBookList);
        mRecyclerViewBook.setNestedScrollingEnabled(false);
        mRecyclerViewBook.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewBook.setAdapter(mBookAdapter);
        mBtnSortByDate.setTextColor(getColor(android.R.color.tab_indicator_text));
        mBtnSortByRate.setTextColor(getColor(android.R.color.tab_indicator_text));
        mBtnSortBySale.setTextColor(getColor(android.R.color.tab_indicator_text));
        mBtnSortByPrice.setTextColor(getColor(android.R.color.tab_indicator_text));
    }

    private void setListener() {
        mBtnBack.setOnClickListener(v -> finish());
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
        mBtnSortByRate.setOnClickListener(v -> onSort(ORDER_BY_RATE));
        mBtnSortByDate.setOnClickListener(v -> onSort(ORDER_BY_DATE));
        mBtnSortBySale.setOnClickListener(v -> onSort(ORDER_BY_SALE));
        mBtnSortByPrice.setOnClickListener(v -> onSort(ORDER_BY_PRICE));
    }

    private void initData() {
        mCustomer = new SharePreferencesUtils(getApplicationContext()).getAccountCustomer();
        mCategory = getIntent().getParcelableExtra(CATEGORY_KEY);
        if (mCategory != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mPresenter.loadBookByCategoryId(mCategory.getId());
        }

    }

    @SuppressLint({"NotifyDataSetChanged", "ResourceAsColor"})
    private void onSort(int option) {
        mProgressBar.setVisibility(View.VISIBLE);
        switch (option) {
            case ORDER_BY_RATE:
                if (!isSortingRate) {
                    isSortingRate = true;
                    isSortingDate = false;
                    isSortingSale = false;
                    isSortingPrice = false;
                    mBookList.sort(Comparator.comparing(Book::getAvgRated));
                    break;
                }
                isSortingRate = false;
                isSortingDate = false;
                isSortingSale = false;
                isSortingPrice = false;
                mBookList.sort(Comparator.comparing(Book::getId));
                break;
            case ORDER_BY_DATE:
                if (!isSortingDate) {
                    isSortingDate = true;
                    isSortingRate = false;
                    isSortingSale = false;
                    isSortingPrice = false;
                    mBookList.sort(Comparator.comparing(Book::getCreatedAt));
                    break;
                }
                isSortingRate = false;
                isSortingDate = false;
                isSortingSale = false;
                isSortingPrice = false;
                mBookList.sort(Comparator.comparing(Book::getId));
                break;
            case ORDER_BY_SALE:
                if (!isSortingSale) {
                    isSortingSale = true;
                    isSortingRate = false;
                    isSortingDate = false;
                    isSortingPrice = false;
                    mBookList.sort(Comparator.comparing(Book::getTotalBookSelled));
                    break;
                }
                isSortingRate = false;
                isSortingDate = false;
                isSortingSale = false;
                isSortingPrice = false;
                mBookList.sort(Comparator.comparing(Book::getId));
                break;
            case ORDER_BY_PRICE:
                if (!isSortingPrice) {
                    isSortingRate = false;
                    isSortingDate = false;
                    isSortingSale = false;
                    if (isLowPrice) {
                        isLowPrice = false;
                        mBookList.sort(Comparator.comparing(Book::getSaleOffPrice));
                    } else {
                        isLowPrice = true;
                        mBookList.sort(Comparator.comparing(Book::getSaleOffPrice).reversed());
                        isSortingPrice = true;
                    }
                    break;
                }
                isSortingRate = false;
                isSortingDate = false;
                isSortingSale = false;
                isSortingPrice = false;
                mBookList.sort(Comparator.comparing(Book::getId));
                break;
        }
        mBtnSortByDate.setTextColor(isSortingDate ? getColor(R.color.important_button_color): getColor(android.R.color.tab_indicator_text));
        mBtnSortByRate.setTextColor(isSortingRate ? getColor(R.color.important_button_color): getColor(android.R.color.tab_indicator_text));
        mBtnSortBySale.setTextColor(isSortingSale ? getColor(R.color.important_button_color): getColor(android.R.color.tab_indicator_text));
        mBtnSortByPrice.setTextColor(isSortingPrice ? getColor(R.color.important_button_color): getColor(android.R.color.tab_indicator_text));
        mBookAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void popSnackbarNotification(int messageResId) {
        WidgetUtils.showSnackbar(findViewById(R.id.main_layout), messageResId);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void loadBooks(List<Book> bookList) {
        if (bookList == null) {
            popSnackbarNotification(R.string.txt_book_not_found_by_category);
            return;
        }
        mBookList.clear();
        mBookList.addAll(bookList);
        mBookAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}