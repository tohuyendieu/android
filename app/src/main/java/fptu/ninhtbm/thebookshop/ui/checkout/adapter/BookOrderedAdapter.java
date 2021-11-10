package fptu.ninhtbm.thebookshop.ui.checkout.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.Utils;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.model.BookSelected;
import fptu.ninhtbm.thebookshop.ui.bookdetail.BookDetailActivity;

public class BookOrderedAdapter extends RecyclerView.Adapter<BookOrderedAdapter.ViewHolder> {

    private final List<BookSelected> mBookSelectedList;
    private Context mContext;

    public BookOrderedAdapter(Context context, List<BookSelected> bookSelectedList) {
        mContext = context;
        mBookSelectedList = bookSelectedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mini_item_cart, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        BookSelected bookSelected = mBookSelectedList.get(position);
        if (bookSelected == null) return;
        Book book = (Book) bookSelected.getBookID();
        holder.textTitle.setText(book.getTitle());
        holder.textPrice.setText(Utils.convertCurrency(book.getSaleOffPrice()));
        Glide.with(mContext)
                .load(book.getBookCoverImg())
                .into(holder.imgBook);
        holder.edtQuantity.setText(mContext.getString(R.string.txt_string_quantity, String.valueOf(bookSelected.getQuantity())));
    }

    @Override
    public int getItemCount() {
        return mBookSelectedList.size();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgBook;
        private final TextView textTitle;
        private final TextView textPrice;
        private final TextView edtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook = itemView.findViewById(R.id.img_book);
            textTitle = itemView.findViewById(R.id.text_title);
            textPrice = itemView.findViewById(R.id.text_price);
            edtQuantity = itemView.findViewById(R.id.edt_quantity);

        }
    }
}