package fptu.ninhtbm.thebookshop.ui.bookdetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.Constants;
import fptu.ninhtbm.thebookshop.library.DateUtils;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.Utils;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Author;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.Category;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.model.Publisher;
import fptu.ninhtbm.thebookshop.model.Stock;
import fptu.ninhtbm.thebookshop.ui.bookdetail.presenter.BookDetailPresenter;
import fptu.ninhtbm.thebookshop.ui.bookdetail.presenter.IBookDetailPresenter;
import fptu.ninhtbm.thebookshop.ui.login.LoginActivity;

public class BookDetailActivity extends AppCompatActivity implements IBookDetailActivity {

    public static final String BOOK_ID_KEY = "BOOK_DATA_KEY";

    private SwipeRefreshLayout mSwipeContainer;
    private ImageView mImgBook;
    private TextView mTextBookTitle;
    private TextView mTextAuthor;
    private TextView mTextPrice;
    private TextView mTextOriginalPrice;
    private RatingBar mRatingbar;
    private TextView mTextAvgRate;
    private TextView mTextTotalSold;
    private TextView mTextStock;
    private TextView mTextCategory;
    private TextView mTextPublisher;
    private TextView mTextPublishDate;
    private TextView mTextTotalPage;
    private TextView mTextBookDescription;
    private TextView mBtnShowMoreBookDescription;
    private TextView mTextAuthorDescription;
    private TextView mBtnShowMoreAuthorDescription;
    private TextView mBtnRate;
    private TextView mBtnAddToCart;


    private IBookDetailPresenter mPresenter;
    private Customer mCustomer;
    private Book mBook;

    private final ActivityResultLauncher<Intent> loginBeforeAddToCartResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    mCustomer = new SharePreferencesUtils(getApplicationContext()).getAccountCustomer();
                    mPresenter.addBookToCart(mCustomer.getId(), mBook.getId());
                    popSnackbarNotification(R.string.txt_add_to_cart_success);
                } else {
                    popSnackbarNotification(R.string.txt_add_to_cart_login_required);
                }
            });
    private boolean isBookDescriptionExpanded;
    private boolean isAuthorDescriptionExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initViews();
        setListener();
        initData();
    }

    private void initData() {
        mCustomer = new SharePreferencesUtils(this).getAccountCustomer();
        String bookId = getIntent().getStringExtra(BOOK_ID_KEY);
        if (bookId == null) {
            WidgetUtils.showSnackbar(mSwipeContainer, R.string.txt_noti_loading_failure);
            return;
        }

        mPresenter.loadBookById(bookId);
    }

    private void initViews() {
        mPresenter = new BookDetailPresenter(this);

        mSwipeContainer = findViewById(R.id.swipe_container);
        mImgBook = findViewById(R.id.img_book);
        mTextBookTitle = findViewById(R.id.text_book_title);
        mTextAuthor = findViewById(R.id.text_author);
        mTextPrice = findViewById(R.id.text_price);
        mTextOriginalPrice = findViewById(R.id.text_original_price);
        mRatingbar = findViewById(R.id.rate_bar);
        mTextAvgRate = findViewById(R.id.text_avg_rate);
        mTextTotalSold = findViewById(R.id.text_total_sold);
        mTextStock = findViewById(R.id.text_stock);
        mTextCategory = findViewById(R.id.text_category);
        mTextPublisher = findViewById(R.id.text_publisher);
        mTextPublishDate = findViewById(R.id.text_publisher_date);
        mTextTotalPage = findViewById(R.id.text_total_page);
        mTextBookDescription = findViewById(R.id.text_book_description);
        mBtnShowMoreBookDescription = findViewById(R.id.btn_show_more_book_description);
        mTextAuthorDescription = findViewById(R.id.text_author_description);
        mBtnShowMoreAuthorDescription = findViewById(R.id.btn_show_more_author_description);
        mBtnRate = findViewById(R.id.btn_rate);
        mBtnAddToCart = findViewById(R.id.btn_add_to_cart);
    }

    private void setListener() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        mSwipeContainer.setOnRefreshListener(() -> {
            mPresenter.loadBookById(mBook.getId());
            Handler handler = new Handler();
            final Runnable r = () -> mSwipeContainer.setRefreshing(false);
            handler.postDelayed(r, 2000);
        });
        mBtnShowMoreBookDescription.setOnClickListener(v -> {
            if (isBookDescriptionExpanded) {
                isBookDescriptionExpanded = false;
                mTextBookDescription.setMaxLines(5);
                mTextBookDescription.setEllipsize(TextUtils.TruncateAt.END);
                mBtnShowMoreBookDescription.setText(getString(R.string.txt_view_more));
                mBtnShowMoreBookDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
            } else {
                isBookDescriptionExpanded = true;
                mTextBookDescription.setEllipsize(TextUtils.TruncateAt.START);
                mTextBookDescription.setMaxLines(9);
                mBtnShowMoreBookDescription.setText(getString(R.string.txt_view_less));
                mBtnShowMoreBookDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
            }
        });
        mBtnShowMoreAuthorDescription.setOnClickListener(v -> {
            if (isAuthorDescriptionExpanded) {
                isAuthorDescriptionExpanded = false;
                mTextAuthorDescription.setMaxLines(5);
                mTextAuthorDescription.setEllipsize(TextUtils.TruncateAt.END);
                mBtnShowMoreAuthorDescription.setText(getString(R.string.txt_view_more));
                mBtnShowMoreAuthorDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
            } else {
                isAuthorDescriptionExpanded = true;
                mTextAuthorDescription.setMaxLines(99);
                mTextAuthorDescription.setEllipsize(TextUtils.TruncateAt.START);
                mBtnShowMoreAuthorDescription.setText(getString(R.string.txt_view_less));
                mBtnShowMoreAuthorDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
            }
        });
        mBtnRate.setOnClickListener(this::onRate);
        mBtnAddToCart.setOnClickListener(this::onAddToCart);
    }

    private void onRate(View view) {
        final Dialog dialog = WidgetUtils.getCustomDialogSetUp(this, R.layout.rating_layout);
        ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        RatingBar ratingBar = dialog.findViewById(R.id.rate_bar);
        MaterialButton btnRate = dialog.findViewById(R.id.btn_rate);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        btnRate.setOnClickListener(v -> {
            int ratedStar = (int) ratingBar.getRating();
            mPresenter.rateBook(mBook.getId(), ratedStar);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void onAddToCart(View view) {
        if (mCustomer == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.IS_REQUEST_FROM_BOOK_DETAIL, true);
            loginBeforeAddToCartResultLauncher.launch(intent);
            return;
        }
        mPresenter.addBookToCart(mCustomer.getId(), mBook.getId());
        popSnackbarNotification(R.string.txt_add_to_cart_success);
    }

    private void loadBookData() {
        mPresenter.getAllAuthorByBookId(mBook.getId());
        Glide.with(getApplicationContext())
                .load(mBook.getBookCoverImg())
                .into(mImgBook);

        mTextBookTitle.setText(mBook.getTitle());
        mTextPrice.setText(Utils.convertCurrency(mBook.getSaleOffPrice()));
        mTextOriginalPrice.setText(Utils.convertCurrency(mBook.getPrice()));
        mTextOriginalPrice.setPaintFlags(mTextOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mRatingbar.setRating((float) mBook.getAvgRated());
        if (mBook.getAvgRated() > 0) {
            mTextAvgRate.setText(new DecimalFormat("#0.0").format(mBook.getAvgRated()));
        } else {
            mTextAvgRate.setText(getString(R.string.txt_no_rate));
        }
        mTextTotalSold.setText(getString(R.string.txt_string_book_sold, String.valueOf(mBook.getTotalBookSelled())));
        Stock bookStock = (Stock) mBook.getStockID();
        mTextStock.setEnabled(bookStock.getQuantity() > 0);
        mTextStock.setText(bookStock.getQuantity() > 0 ? getString(R.string.txt_in_stock) : getString(R.string.txt_out_of_stock));
        Category bookCategory = (Category) mBook.getCategoryID();
        mTextCategory.setText(bookCategory.getTitle());
        Publisher bookPublisher = (Publisher) mBook.getPublisherID();
        mTextPublisher.setText(bookPublisher.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mTextPublishDate.setText(DateUtils.convertTimestampToDateFormat(mBook.getCreatedAt(), Constants.DATE_DD_MM_YYYY));
        } else {
            mTextPublishDate.setText(mBook.getCreatedAt().toDate().toString());
        }
        mTextTotalPage.setText(String.valueOf(mBook.getTotalPage()));
        mTextBookDescription.setText(mBook.getDescription());
    }

    @Override
    public void loadBook(Book book) {
        mBook = book;
        loadBookData();

    }

    @Override
    public void popSnackbarNotification(int messageResId) {
        WidgetUtils.showSnackbar(mSwipeContainer, messageResId);
    }

    @Override
    public void ratedSuccess() {
        mPresenter.loadBookById(mBook.getId());
        popSnackbarNotification(R.string.txt_rated_success);
    }

    @SuppressLint("NewApi")
    @Override
    public void loadAuthors(List<Author> authorList) {
        if (authorList.size() == 1) {
            Author author = authorList.get(0);
            mTextAuthor.setText(author.getName());
            if (!TextUtils.isEmpty(author.getDescription())) {
                mTextAuthorDescription.setText(author.getDescription());
            }

        } else {
            List<String> authorName = new ArrayList<>();
            String allDescription = "";
            for (Author a : authorList) {
                authorName.add(a.getName());
                if (!TextUtils.isEmpty(a.getDescription())) {
                    allDescription += a.getName();
                    allDescription += "\n" + a.getDescription() + "\n\n\n";
                }
            }
            String result = String.join(",", authorName);
            mTextAuthor.setText(result);
            mTextBookDescription.setText(allDescription);

        }
    }
}