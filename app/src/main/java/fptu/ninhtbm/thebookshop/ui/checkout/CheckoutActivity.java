package fptu.ninhtbm.thebookshop.ui.checkout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.Constants;
import fptu.ninhtbm.thebookshop.library.SharePreferencesUtils;
import fptu.ninhtbm.thebookshop.library.Utils;
import fptu.ninhtbm.thebookshop.library.WidgetUtils;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.model.Customer;
import fptu.ninhtbm.thebookshop.model.Order;
import fptu.ninhtbm.thebookshop.ui.checkout.adapter.BookOrderedAdapter;
import fptu.ninhtbm.thebookshop.ui.checkout.presenter.CheckoutPresenter;
import fptu.ninhtbm.thebookshop.ui.checkout.presenter.ICheckoutPresenter;
import fptu.ninhtbm.thebookshop.ui.home.MainActivity;

public class CheckoutActivity extends AppCompatActivity implements ICheckoutActivity {


    private RecyclerView mRecyclerViewBook;
    private EditText mEdtName;
    private EditText mEdtPhone;
    private EditText mEdtAddress;
    private TextView mTextTotalCost;
    private TextView mBtnCheckout;

    private ICheckoutPresenter mPresenter;
    private BookOrderedAdapter mBookOrderedAdapter;
    private List<BookSelected> mBookSelectedList;

    private Customer mCustomer;

    private double mTotalCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initView();
        setListener();
        initData();
    }

    private void setListener() {
        mBtnCheckout.setOnClickListener(this::onCheckout);
    }

    private void initData() {
        mPresenter = new CheckoutPresenter(this);
        mCustomer = new SharePreferencesUtils(getApplicationContext()).getAccountCustomer();
        mPresenter.loadAllBookInCart(mCustomer.getId());
        mEdtName.setText(mCustomer.getName());
        mEdtPhone.setText(mCustomer.getPhone());
        mEdtAddress.setText(mCustomer.getAddress());


    }

    private void initView() {
        mRecyclerViewBook = findViewById(R.id.rv_cart);
        mEdtName = findViewById(R.id.edt_name);
        mEdtPhone = findViewById(R.id.edt_phone);
        mEdtAddress = findViewById(R.id.edt_address);
        mTextTotalCost = findViewById(R.id.text_total_cost);
        mBtnCheckout = findViewById(R.id.btn_buy);

        mBookSelectedList = new ArrayList<>();
        mBookOrderedAdapter = new BookOrderedAdapter(this, mBookSelectedList);
        mRecyclerViewBook.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewBook.setAdapter(mBookOrderedAdapter);
        ((TextView) findViewById(R.id.text_title)).setText(getString(R.string.txt_button_checkout));
    }


    @Override
    public void popSnackbarNotification(int messageResId) {
        WidgetUtils.showSnackbar(findViewById(R.id.layout_main), messageResId);
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
        mTotalCost = totalCost;
        mTextTotalCost.setText(Utils.convertCurrency(totalCost));
        mBookOrderedAdapter.notifyDataSetChanged();
    }

    @Override
    public void checkoutSuccess() {
        WidgetUtils.showDialogSuccessCheckedGreen(CheckoutActivity.this,
                getString(R.string.txt_checkout_success),
                this::onBackToMain).show();
    }

    void onCheckout(View view) {
        String name = mEdtName.getText().toString().trim();
        String phone = mEdtPhone.getText().toString().trim();
        String address = mEdtAddress.getText().toString().trim();
        Order order = new Order(mCustomer, FirebaseFirestore.getInstance().collection(Constants.TABLE_ORDER_STATUS).document(Constants.STATUS_USER_ORDERED), new Timestamp(new Date()), mTotalCost, name, phone, address);
        mPresenter.checkoutCart(mBookSelectedList, order);
    }

    void onBackToMain(View view) {
        Intent i = new Intent(CheckoutActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}