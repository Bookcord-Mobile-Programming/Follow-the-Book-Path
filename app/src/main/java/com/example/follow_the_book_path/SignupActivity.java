package com.example.follow_the_book_path;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 필드 설정
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // 회원가입 버튼 클릭 리스너
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // 필수 입력값 확인
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "이름, 이메일, 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 이메일 형식 확인
                if (!isValidEmail(email)) {
                    Toast.makeText(SignupActivity.this, "유효한 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 암호화

                // 회원가입 처리
                if (addUserToDatabase(name, email, password)) {
                    Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    // 회원가입 성공 후 MainActivity로 이동
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignupActivity.this, "회원가입 실패: 이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean addUserToDatabase(String name, String email, String password) {
        Cursor cursor = null;

        try {
            // 이메일을 소문자로 변환하여 비교
            email = email.toLowerCase();

            // 이메일 중복 검사
            cursor = db.query("user", null, "email = ?", new String[]{email}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                // 이미 이메일이 존재하는 경우
                Log.d("SignupActivity", "중복된 이메일: " + email);
                return false;  // 중복된 이메일
            }

            // 중복이 없으면 사용자 정보 삽입
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("email", email);
            values.put("password", password);

            // 사용자 데이터 삽입
            long result = db.insert("user", null, values);

            // 삽입 결과 확인
            if (result != -1) {
                Log.d("SignupActivity", "회원가입 성공: " + email);
                return true;  // 삽입 성공
            } else {
                Log.e("SignupActivity", "회원가입 실패: 삽입 실패");
                return false;  // 삽입 실패
            }
        } catch (Exception e) {
            Log.e("SignupActivity", "에러 발생: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();  // 커서 닫기
            }
        }
    }

    // 이메일 형식 검사
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

}
