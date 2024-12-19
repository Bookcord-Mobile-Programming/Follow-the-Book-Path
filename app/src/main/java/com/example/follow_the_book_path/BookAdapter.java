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

        // 제목, 저자, 이미지 설정
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.bookImageView.setImageResource(book.getImageResId());

        // 날짜 형식 변환 및 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 형식
        String formattedDate = sdf.format(book.getDate()); // Date 객체 -> 문자열
        holder.dateTextView.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // ViewHolder 클래스
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, dateTextView;
        ImageView bookImageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            bookImageView = itemView.findViewById(R.id.book_image_view);
        }
    }
}
