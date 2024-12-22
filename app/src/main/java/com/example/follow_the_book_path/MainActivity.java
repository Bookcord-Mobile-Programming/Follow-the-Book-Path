package com.example.follow_the_book_path;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
<<<<<<< HEAD
=======
import android.widget.TextView;
>>>>>>> b844a2c (임시커밋)

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        //연결
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        //회원가입 버튼 클릭 리스너 설정
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이메일 패스워드 정보 가져오기
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (validateLogin(email, password)) {
                    // 로그인 성공시 홈 화면으로 이동
                    Intent intent = new Intent(MainActivity.this, MainPageActivity.class);

                    startActivity(intent);
                } else {
                    // 로그인 실패시 메시지 출력
                    Toast.makeText(MainActivity.this, "로그인 실패: 이메일 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateLogin(String email, String password) {
        boolean isValid = false;

        try {
            String[] columns = {"userId", "email", "password"};
            String selection = "email = ? AND password = ?";
            String[] selectionArgs = {email, password};

            Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null);

            Log.d("DB", "Cursor count: " + cursor.getCount());

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();  // 첫 번째 행으로 이동
                @SuppressLint("Range") String retrievedEmail = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String retrievedPassword = cursor.getString(cursor.getColumnIndex("password"));
                Log.d("DB", "Retrieved email: " + retrievedEmail + ", password: " + retrievedPassword);
                isValid = true;
            } else {
                Log.d("DB", "No matching data found");
            }

            cursor.close();
        } catch (Exception e) {
            Log.e("DBError", "로그인 검증 중 오류 발생", e);
        }

        return isValid;
    }

}

