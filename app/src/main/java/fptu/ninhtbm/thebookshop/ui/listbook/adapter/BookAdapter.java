package fptu.ninhtbm.thebookshop.ui.listbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;
import fptu.ninhtbm.thebookshop.library.Utils;
import fptu.ninhtbm.thebookshop.model.Author;
import fptu.ninhtbm.thebookshop.model.AuthorBook;
import fptu.ninhtbm.thebookshop.model.Book;
import fptu.ninhtbm.thebookshop.ui.bookdetail.BookDetailActivity;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final List<Book> mBookList;
    private Context mContext;

    public BookAdapter(Context context, List<Book> bookList) {
        mContext = context;
        mBookList = bookList;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_book, viewGroup, false);
        return new BookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        if (mBookList == null || mBookList.get(position) == null)
            return;
        holder.setIsRecyclable(false);
        Book book = mBookList.get(position);
        Glide.with(mContext)
                .load(book.getBookCoverImg())
                .into(holder.imgBook);
        holder.textTitle.setText(book.getTitle());
        setAuthorsByBookId(book.getId(), holder);
        holder.textPrice.setText(Utils.convertCurrency(book.getSaleOffPrice()));
        holder.textOriginalPrice.setText(Utils.convertCurrency(book.getPrice()));
        holder.btnAddToCart.setOnClickListener(v -> onViewDetail(book.getId()));
        holder.itemView.setOnClickListener(v -> onViewDetail(book.getId()));
    }

    private void onViewDetail(String bookId) {
        Intent intent = new Intent(mContext, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.BOOK_ID_KEY, bookId);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public void setAuthorsByBookId(String bookId, ViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AuthorBook")
                .whereEqualTo("bookID", db.collection("Book").document(bookId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot authorBookDocs = task.getResult();
                        if (authorBookDocs.getDocuments().size() > 0) {
                            List<Author> authorList = new ArrayList<>();

                            for (int i = 0; i < authorBookDocs.getDocuments().size(); i++) {
                                final int index = i;
                                AuthorBook authorBook = authorBookDocs.getDocuments().get(index).toObject(AuthorBook.class);
                                authorBook.setId(authorBookDocs.getDocuments().get(index).getId());
                                ((DocumentReference) authorBook.getAuthorID())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot doc = task1.getResult();
                                                if (doc.exists()) {
                                                    Author author = doc.toObject(Author.class);
                                                    author.setId(doc.getId());

                                                    authorList.add(author);
                                                } else {
                                                    Log.d("ListBook", mContext.getString(R.string.txt_author_not_found));
                                                }
                                            } else {
                                                Log.d("ListBook", mContext.getString(R.string.txt_noti_loading_failure));
                                            }
                                            // Return data if end for loop
                                            if (index == authorBookDocs.getDocuments().size() - 1) {
                                                if (authorList.size() == 1) {
                                                    holder.textAuthor.setText(authorList.get(0).getName());
                                                } else {
                                                    List<String> authorName = new ArrayList<>();
                                                    for (Author a : authorList) {
                                                        authorName.add(a.getName());
                                                    }
                                                    String result = null;
                                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                        result = String.join(",", authorName);
                                                    }

                                                    holder.textAuthor.setText(TextUtils.isEmpty(result) ? "" : result);
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d("ListBook", mContext.getString(R.string.txt_author_not_found));
                        }
                    } else {
                        Log.d("ListBook", mContext.getString(R.string.txt_noti_loading_failure));
                    }
                });
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final ImageView imgBook;
        private final TextView textTitle;
        private final TextView textAuthor;
        private final TextView textOriginalPrice;
        private final TextView textPrice;
        private final ExtendedFloatingActionButton btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imgBook = itemView.findViewById(R.id.img_book);
            textTitle = itemView.findViewById(R.id.text_book_title);
            textAuthor = itemView.findViewById(R.id.text_author);
            textOriginalPrice = itemView.findViewById(R.id.text_original_price);
            textOriginalPrice.setPaintFlags(textOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textPrice = itemView.findViewById(R.id.text_discount_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
