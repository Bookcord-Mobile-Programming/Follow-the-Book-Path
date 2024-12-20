package com.example.follow_the_book_path;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    // 생성자
    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.genreTextView.setText(book.getGenre());
        holder.statusTextView.setText(book.getStatus());
        holder.bookImageView.setImageResource(book.getImageResId());

        // 날짜 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        holder.startDateTextView.setText(sdf.format(book.getStartDate()));
        holder.endDateTextView.setText(sdf.format(book.getEndDate()));
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // ViewHolder 클래스
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, genreTextView, statusTextView;
        TextView startDateTextView, endDateTextView;
        ImageView bookImageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            genreTextView = itemView.findViewById(R.id.genre_text_view);
            statusTextView = itemView.findViewById(R.id.status_text_view);
            startDateTextView = itemView.findViewById(R.id.start_date_text_view);
            endDateTextView = itemView.findViewById(R.id.end_date_text_view);
            bookImageView = itemView.findViewById(R.id.book_image_view);
        }
    }

}
