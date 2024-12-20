package com.example.follow_the_book_path;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class bookListforTest extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher; // [테스트용] intent형 activityResultLauncher 객체 생성
    int userId = 1; // [테스트용] 유저아이디
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    ListView list; // [테스트용] 책 리스트뷰 선언
    private ArrayList<String> bookNameList = new ArrayList<>(); // 비어 있는 책이름 리스트 선언 (동적 추가)
    private ArrayList<Integer> bookIdList = new ArrayList<>(); // 비어 있는 북아이디 리스트 선언 (동적 추가)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_test);
        setTitle("내 서재 테스트용");

        // 책 리스트뷰 (리스트뷰 초기화는 setContentView 이후에 수행해야 함)
        list = (ListView) findViewById(R.id.listView1);

        loadBook();
        bookRecordOrUpdate();
    }

    // [테스트용] 책 리스트 띄우기
    private void loadBook() {
        // 기존 데이터 초기화
        bookIdList.clear();
        bookNameList.clear();

        // 데이터베이스 연결
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT bookId, bookName FROM book", null); // 책 이름 선택

        if (cursor.moveToFirst()) {
            do {
                bookIdList.add(cursor.getInt(cursor.getColumnIndexOrThrow("bookId"))); // 책 아이디들 리스트뷰에 추가
                bookNameList.add(cursor.getString(cursor.getColumnIndexOrThrow("bookName")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookNameList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 변경 사항 반영
    }

    // [테스트용] 책 신규 추가하거나 책 선택 시 상세 정보 페이지로 이동
    private void bookRecordOrUpdate() {
        // activityResultLauncher 초기화
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) { // resultCode가 ok로 넘어왔다면
                Intent intent = result.getData(); // ActivityResult 객체 result로 intent를 받아온다.
                String name = intent.getStringExtra("result");
                loadBook(); // 책 리스트 갱신
            }
        });

        // 신규 책 등록 버튼
        Button btnAddBook = findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(v -> { // bookRecordActivity에게 보내는 인텐트
            Intent intent = new Intent(bookListforTest.this, bookRecordActivity.class);
            intent.putExtra("userId", userId); // 유저 ID 전달
            intent.putExtra("bookId", -1); // 북 ID -1 (신규)로 전달
            activityResultLauncher.launch(intent); // Activity 실행
        });

        // 책 리스트뷰 클릭 리스너 -> 상세 정보 페이지로 인텐트 보내기
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedBookId = bookIdList.get(position); // 선택된 책의 ID 가져옴

                // bookRecordActivity에게 보내는 인텐트
                Intent intent = new Intent(bookListforTest.this, bookRecordActivity.class);
                intent.putExtra("userId", userId); // 유저 ID 전달
                intent.putExtra("bookId", selectedBookId); // 책 ID 전달
                activityResultLauncher.launch(intent); // Activity 실행
            }
        });
    }
}
