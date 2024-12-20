package com.example.follow_the_book_path;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        // 책 리스트 초기화
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList);
        recyclerView.setAdapter(bookAdapter);

        // 초기 데이터 삽입 (테스트용)
        insertDummyData();

        // 데이터베이스에서 책 데이터 로드
        loadBooksFromDatabase();

        // 정렬 버튼 설정
        Button sortTitleButton = findViewById(R.id.sort_title_button);
        sortTitleButton.setOnClickListener(v -> {
            // 제목순 정렬
            bookList.sort((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()));
            bookAdapter.notifyDataSetChanged();
        });

        Button sortStartDateButton = findViewById(R.id.sort_date_button);
        sortStartDateButton.setOnClickListener(v -> {
            // 시작 날짜순 정렬
            bookList.sort((b1, b2) -> b1.getStartDate().compareTo(b2.getStartDate()));
            bookAdapter.notifyDataSetChanged();
        });

        // 책 추가 버튼 동작 추가
        Button btnAddBook = findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, bookRecordActivity.class);
            intent.putExtra("userId", 1); // 현재 로그인한 사용자의 ID (예시로 1 설정)
            intent.putExtra("bookId", -1); // -1은 새 책 추가를 의미
            startActivity(intent); // bookRecordActivity 실행
        });
    }

    // 데이터베이스에서 책 데이터를 불러오는 메서드
    private void loadBooksFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM book", null);
        bookList.clear();

        if (cursor.moveToFirst()) {
            do {
                try {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("bookName"));
                    String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    String genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));

                    // 날짜 변환
                    String startDateStr = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
                    String endDateStr = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = startDateStr != null ? sdf.parse(startDateStr) : null;
                    Date endDate = endDateStr != null ? sdf.parse(endDateStr) : null;

                    // 리스트에 추가
                    bookList.add(new Book(title, author, genre, startDate, endDate, status, imageResId));
                } catch (Exception e) {
                    Log.e("LibraryActivity", "Error parsing book data", e);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // 어댑터에 데이터 변경 알림
        bookAdapter.notifyDataSetChanged();
    }

    // 초기 데이터를 삽입하는 메서드
    private void insertDummyData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("INSERT INTO book (bookName, author, genre, startDate, endDate, status, imageResId, userId) VALUES " +
                "('책1', '저자1', '소설', '2023-01-01', '2023-01-10', '완료', " + R.drawable.ic_launcher_foreground + ", 1), " +
                "('책2', '저자2', '에세이', '2022-05-01', '2022-05-15', '읽는 중', " + R.drawable.ic_launcher_foreground + ", 1);");

        db.close();
    }
}
