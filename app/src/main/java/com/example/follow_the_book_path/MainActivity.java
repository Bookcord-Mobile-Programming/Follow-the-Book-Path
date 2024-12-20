package com.example.follow_the_book_path;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 내 서재 버튼 클릭 리스너
        Button myLibrary = findViewById(R.id.myLibrary);
        myLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LibraryActivity로 이동
                Intent intent = new Intent(MainActivity.this, bookListforTest.class);
                startActivity(intent);
            }
        });

    }

}