package fptu.ninhtbm.thebookshop.ui.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.Utils;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.ui.cart.adapter.BookSelectedAdapter;
import fptu.ninhtbm.thebookshop.ui.cart.presenter.CartPresenter;
import fptu.ninhtbm.thebookshop.ui.cart.presenter.ICartPresenter;
import fptu.ninhtbm.thebookshop.ui.checkout.CheckoutActivity;

public class CartActivity extends AppCompatActivity implements ICartActivity {

    private RecyclerView mRecyclerViewCart;
    private BookSelectedAdapter mBookSelectedAdapter;
    private CircularProgressIndicator mProgressBar;
    private TextView mTextTotalCost;
    private TextView mBtnBuy;

    private ICartPresenter mPresenter;
    private Customer mCustomer;

    private List<BookSelected> mBookSelectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
        setListener();
        initData();
    }

    private void initViews() {
        mPresenter = new CartPresenter(this);
        mRecyclerViewCart = findViewById(R.id.rv_cart);
        mProgressBar = findViewById(R.id.progress_bar);
        mTextTotalCost = findViewById(R.id.text_total_cost);
        mBtnBuy = findViewById(R.id.btn_buy);
        mBookSelectedList = new ArrayList<>();
        mBookSelectedAdapter = new BookSelectedAdapter(this, mBookSelectedList);
        mRecyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewCart.setAdapter(mBookSelectedAdapter);
    }

    private void setListener() {
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        mBtnBuy.setOnClickListener(this::onBuy);
        mBookSelectedAdapter.setOnChangeQuantityListener(new BookSelectedAdapter.OnChangeQuantityListener() {
            @Override
            public void onChangeQuantity(BookSelected bookSelected, int number) {
                mPresenter.updateQuantityBookSelected(bookSelected, number);
            }

            @Override
            public void removeBookSelected(String bookSelectedId) {
                mPresenter.deleteBookSelectedById(bookSelectedId);
            }
        });
    }

    private void onBuy(View view) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    private void initData() {
        mCustomer = new SharePreferencesUtils(getApplicationContext()).getAccountCustomer();
        if (mCustomer == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_load_account_in_cart_fail), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mPresenter.loadAllBookInCart(mCustomer.getId());
        TextView textTitle = findViewById(R.id.text_title);
        textTitle.setText(getString(R.string.txt_cart));
    }

    @Override
    public void popSnackbarNotification(int messageResId) {
        WidgetUtils.showSnackbar(mRecyclerViewCart, messageResId);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void loadBookSelectedInCart(List<BookSelected> bookSelectedList) {
        mBookSelectedList.clear();
        mBookSelectedList.addAll(bookSelectedList);

        double totalCost = 0;
        for (BookSelected bs : bookSelectedList) {
            Book book = (Book) bs.getBookID();
            totalCost += book.getSaleOffPrice() * bs.getQuantity();
        }
        mTextTotalCost.setText(Utils.convertCurrency(totalCost));
        mBookSelectedAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.INVISIBLE);
        if (mBookSelectedList.isEmpty()) {
            findViewById(R.id.text_cart_no_item).setVisibility(View.VISIBLE);
            mBtnBuy.setEnabled(false);
            mRecyclerViewCart.setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.text_cart_no_item).setVisibility(View.INVISIBLE);
            mBtnBuy.setEnabled(true);
            mRecyclerViewCart.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void reloadData() {
        mPresenter.loadAllBookInCart(mCustomer.getId());
    }
}