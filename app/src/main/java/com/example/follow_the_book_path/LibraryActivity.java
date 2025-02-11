package com.example.follow_the_book_path;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    ActivityResultLauncher<Intent> activityResultLauncher;
    private final int userId = 1; // 예시로 사용자 ID 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.book_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 데이터 초기화
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, this::onBookClicked, this::onBookDeleted);
        recyclerView.setAdapter(bookAdapter);

        // 데이터베이스 연결
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // ActivityResultLauncher 초기화
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // 책 추가/수정 후 데이터 갱신
                loadBooksFromDatabase();
            }
        });

        // 초기 데이터 삽입 (테스트용)
        insertDummyData();

        // 데이터베이스에서 책 데이터 로드
        loadBooksFromDatabase();

        // 정렬 버튼 설정
        Button sortTitleButton = findViewById(R.id.sort_title_button);
        sortTitleButton.setOnClickListener(v -> {
            bookList.sort((b1, b2) -> b1.getTitle().compareTo(b2.getTitle())); // 제목순 정렬
            bookAdapter.notifyDataSetChanged();
        });

        Button sortStartDateButton = findViewById(R.id.sort_date_button);
        sortStartDateButton.setOnClickListener(v -> {
            bookList.sort((b1, b2) -> b1.getStartDate().compareTo(b2.getStartDate())); // 시작 날짜순 정렬
            bookAdapter.notifyDataSetChanged();
        });

        // 독서 메모 버튼 참조 및 이벤트 추가
        Button goToMemoButton = findViewById(R.id.go_to_memo_button);
        goToMemoButton.setOnClickListener(v -> {
            // MemoActivity로 이동
            Intent intent = new Intent(LibraryActivity.this, MemoActivity.class);
            startActivity(intent);
        });

        // 책 추가 버튼 설정
        Button btnAddBook = findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(LibraryActivity.this, bookRecordActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("bookId", -1); // -1은 새 책 추가 의미
            activityResultLauncher.launch(intent);
        });
    }

    private void onBookDeleted(int position) {
        Book bookToDelete = bookList.get(position);

        // 데이터베이스에서 삭제
        db.execSQL("DELETE FROM book WHERE bookName = ?", new Object[]{bookToDelete.getTitle()});

        // 리스트에서 삭제 및 RecyclerView 갱신
        bookList.remove(position);
        bookAdapter.notifyItemRemoved(position);

        // 삭제 완료 메시지
        Toast.makeText(this, "책이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 데이터베이스에서 책 데이터 로드
    private void loadBooksFromDatabase() {
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
                    int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("bookId")); // bookId 추가
                    bookList.add(new Book(bookId, title, author, genre, startDate, endDate, status, imageResId));
                } catch (Exception e) {
                    Log.e("LibraryActivity", "Error parsing book data", e);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        // 어댑터에 데이터 변경 알림
        bookAdapter.notifyDataSetChanged();
    }



    // 초기 데이터를 삽입 (테스트용)
    private void insertDummyData() {
        db.execSQL("INSERT INTO book (bookName, author, genre, startDate, endDate, status, imageResId, userId) VALUES " +
                "('책1', '저자1', '소설', '2023-01-01', '2023-01-10', '완료', " + R.drawable.bookimage + ", 1), " +
                "('책2', '저자2', '에세이', '2022-05-01', '2022-05-15', '읽는 중', " + R.drawable.bookimage + ", 1);");
    }


    // 책 클릭 이벤트 처리
    private void onBookClicked(int position) {
        Book selectedBook = bookList.get(position);

        Intent intent = new Intent(LibraryActivity.this, bookRecordActivity.class);
        intent.putExtra("userId", userId); // 유저 ID 전달
        intent.putExtra("bookId", selectedBook.getBookId()); // 책 ID 전달
        activityResultLauncher.launch(intent);
    }
}