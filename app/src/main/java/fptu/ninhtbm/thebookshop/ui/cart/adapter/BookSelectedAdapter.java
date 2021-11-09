package fptu.ninhtbm.thebookshop.ui.cart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.Utils;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.ui.bookdetail.BookDetailActivity;

public class BookSelectedAdapter extends RecyclerView.Adapter<BookSelectedAdapter.ViewHolder> {

    private final List<BookSelected> mBookSelectedList;
    private Context mContext;
    private OnChangeQuantityListener onChangeQuantityListener;

    public BookSelectedAdapter(Context context, List<BookSelected> bookSelectedList) {
        mContext = context;
        mBookSelectedList = bookSelectedList;
    }

    public void setOnChangeQuantityListener(OnChangeQuantityListener onChangeQuantityListener) {
        this.onChangeQuantityListener = onChangeQuantityListener;
    }

    private void onViewDetail(String bookId) {
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.BOOK_ID_KEY, bookId);
        mContext.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_cart, viewGroup, false);
        return new BookSelectedAdapter.ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookSelected bookSelected = mBookSelectedList.get(position);
        if (bookSelected == null) return;
        Book book = (Book) bookSelected.getBookID();
        holder.textTitle.setText(book.getTitle());
        if (book.getDiscount() != 0) {
            holder.textOriginalPrice.setText(Utils.convertCurrency(book.getPrice()));
        }
        holder.textPrice.setText(Utils.convertCurrency(book.getSaleOffPrice()));
        Glide.with(mContext)
                .load(book.getBookCoverImg())
                .into(holder.imgBook);
        holder.imgBook.setOnClickListener(v -> onViewDetail(book.getId()));
        holder.edtQuantity.setText(String.valueOf(bookSelected.getQuantity()));
        holder.btnIncrease.setOnClickListener(v -> {
            onChangeQuantityListener.onChangeQuantity(bookSelected, 1);
            bookSelected.setQuantity(bookSelected.getQuantity() + 1);
            notifyDataSetChanged();
        });
        holder.btnDecrease.setOnClickListener(v -> {
            onChangeQuantityListener.onChangeQuantity(bookSelected, -1);
            bookSelected.setQuantity(bookSelected.getQuantity() - 1);
            notifyDataSetChanged();
        });
        holder.btnRemoveCart.setOnClickListener(v -> onChangeQuantityListener.removeBookSelected(bookSelected.getId()));
    }

    @Override
    public int getItemCount() {
        return mBookSelectedList.size();
    }

    public interface OnChangeQuantityListener {
        void onChangeQuantity(BookSelected bookSelected, int number);

        void removeBookSelected(String bookSelectedId);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgBook;
        private final TextView textTitle;
        private final TextView textOriginalPrice;
        private final TextView textPrice;
        private final TextView edtQuantity;
        private final ImageButton btnIncrease;
        private final ImageButton btnDecrease;
        private final FloatingActionButton btnRemoveCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook = itemView.findViewById(R.id.img_book);
            textTitle = itemView.findViewById(R.id.text_title);
            textOriginalPrice = itemView.findViewById(R.id.text_original_price);
            textOriginalPrice.setPaintFlags(textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textPrice = itemView.findViewById(R.id.text_price);
            edtQuantity = itemView.findViewById(R.id.edt_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnRemoveCart = itemView.findViewById(R.id.btn_remove);

        }
    }
}