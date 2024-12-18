package com.example.follow_the_book_path;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> bookRecordLauncher; // intent형 activityResultLauncher 객체 생성
    int userId = 1; // 유저아이디 예시
    int bookId = -1; // 선택된 책 ID (기본값은 -1이고 신규 등록이란 뜻)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookRecord();

    }

    private void bookRecord() {
        // activityResultLauncher 초기화
        bookRecordLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) { // resultCode가 ok로 넘어왔다면
                Intent intent = result.getData(); // ActivityResult 객체 result로 intent를 받아온다.
                String name = intent.getStringExtra("result");
                Toast.makeText(this, "책이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 신규 책 등록 버튼
        Button btnAddBook = findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, bookRecordActivity.class);
            intent.putExtra("userId", userId); // 유저 ID 전달
            bookRecordLauncher.launch(intent); // Activity 실행
        });

        // 기존 책 수정 버튼
        Button btnEdtBook = findViewById(R.id.btnEdtBook);
        btnEdtBook.setOnClickListener(v -> {
            bookId = 5; // 예시로 bookId 설정 (실제로는 선택된 책의 ID)

            Intent intent = new Intent(MainActivity.this, bookRecordActivity.class);
            intent.putExtra("userId", userId); // 유저 ID 전달
            intent.putExtra("bookId", bookId); // 책 ID 전달
            bookRecordLauncher.launch(intent); // Activity 실행
        });
    }
}