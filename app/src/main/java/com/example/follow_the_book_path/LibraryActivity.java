package com.example.follow_the_book_path;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.book_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 예제 데이터 추가
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        bookList = new ArrayList<>();

        try {
            bookList.add(new Book("가나다", "저자1", sdf.parse("2023-01-01"), R.drawable.ic_launcher_foreground));
            bookList.add(new Book("마바사", "저자2", sdf.parse("2020-02-01"), R.drawable.ic_launcher_foreground));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        // 어댑터 설정
        bookAdapter = new BookAdapter(bookList);
        recyclerView.setAdapter(bookAdapter);

        // 정렬 버튼 설정
        Button sortTitleButton = findViewById(R.id.sort_title_button);
        sortTitleButton.setOnClickListener(v -> {
            // 제목순 정렬
            bookList.sort((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()));
            bookAdapter.notifyDataSetChanged();
        });

        Button sortDateButton = findViewById(R.id.sort_date_button);
        sortDateButton.setOnClickListener(v -> {
            // 날짜순 정렬
            bookList.sort((b1, b2) -> b1.getDate().compareTo(b2.getDate()));
            bookAdapter.notifyDataSetChanged();
        });
    }
}
