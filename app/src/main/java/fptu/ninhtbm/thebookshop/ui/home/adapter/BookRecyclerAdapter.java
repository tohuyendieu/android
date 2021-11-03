package fptu.ninhtbm.thebookshop.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.ui.bookdetail.BookDetailActivity;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    private List<Book> mBookList;
    private Context mContext;

    public BookRecyclerAdapter(Context context, List<Book> bookList) {
        mContext = context;
        mBookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_book_preview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mBookList == null || mBookList.get(position) == null)
            return;
        Book book = mBookList.get(position);
        Glide.with(mContext)
                .load(book.getBookCoverImg())
                .into(holder.imgBook);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, BookDetailActivity.class);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final ImageView imgBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imgBook = itemView.findViewById(R.id.img_book);
        }
    }
}
