package com.example.follow_the_book_path;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class bookRecordActivity extends AppCompatActivity {

    EditText edtBookName, edtAuthor, edtGenre, edtStartDate, edtEndDate;
    Spinner spinnerStatus;
    Button btnBookRecord;

    SQLiteDatabase db;
    DatabaseHelper dbHelper;

    int userId;
    int bookId = -1; // 기존 책의 여부 판단 (-1이면 신규 등록)
    String[] statusOptions = {"읽는 중", "완독", "중단"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_record);
        setTitle("책 기록");

        //UI 초기화
        edtBookName = findViewById(R.id.edtBookName);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtGenre = findViewById(R.id.edtGenre);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnBookRecord = findViewById(R.id.btnBookRecord);

        // 데이터베이스 연결
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Spinner 초기화
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);


        // 인텐트 받기 (책 정보가 있다면 북아이디, 유저아이디)
        // 북아이디 있으면 상세 내용 바로 뜨기, 없으면 (신규 책 등록) 모두 빈칸
        Intent inIntent = getIntent();
        userId = inIntent.getIntExtra("userId", -1);

        if (userId == -1) {
            // userId가 없으면 에러
            Toast.makeText(this, "유저 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bookId = inIntent.getIntExtra("bookId", -1);
        if (bookId != -1) { // 기존 책이면 데이터 불러오기
            loadBookData(bookId);
        }

        btnBookRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBookData();
            }
        });
    }

    // 기존 책 정보 불러오기
    private void loadBookData(int bookId) {
        Cursor cursor = db.rawQuery("SELECT * FROM book WHERE bookId = ?", new String[]{String.valueOf(bookId)});

        if (cursor.moveToFirst()) {
            edtBookName.setText(cursor.getString(cursor.getColumnIndexOrThrow("bookName")));
            edtAuthor.setText(cursor.getString(cursor.getColumnIndexOrThrow("author")));
            edtGenre.setText(cursor.getString(cursor.getColumnIndexOrThrow("genre")));
            edtStartDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("startDate")));
            edtEndDate.setText(cursor.getString(cursor.getColumnIndexOrThrow("endDate")));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            // Spinner에서 선택된 상태 설정
            for (int i = 0; i < statusOptions.length; i++) {
                if (statusOptions[i].equals(status)) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
        cursor.close();
    }

    // 책 정보 저장하기
    private void saveBookData() {
        String bookName = edtBookName.getText().toString();
        String author = edtAuthor.getText().toString();
        String genre = edtGenre.getText().toString();
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if (bookName.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "책 제목과 저자는 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bookId == -1) { // 신규 등록
            db.execSQL("INSERT INTO book (bookName, author, genre, startDate, endDate, status, userId) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{bookName, author, genre, startDate, endDate, status, userId});
            Toast.makeText(this, "책이 추가되었습니다.", Toast.LENGTH_SHORT).show();
        } else { // 수정
            db.execSQL("UPDATE book SET bookName=?, author=?, genre=?, startDate=?, endDate=?, status=? WHERE bookid=?",
                    new Object[]{bookName, author, genre, startDate, endDate, status, bookId});
            Toast.makeText(this, "책이 수정되었습니다.", Toast.LENGTH_SHORT).show();
        }

        // 인텐트 반환
        Intent outIntent = new Intent(getApplicationContext(),
                MainActivity.class);
        outIntent.putExtra("result", "success");
        setResult(RESULT_OK, outIntent);
        finish(); // 액티비티 종료

    }

}
