package com.example.follow_the_book_path;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MemoAdapter extends ArrayAdapter<Memo> {
    Context context;
    ArrayList<Memo> memoList;

    // 생성자
    public MemoAdapter(Context context, ArrayList<Memo> memos) {
        super(context, 0, memos);
        this.context = context;
        this.memoList = memos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView 초기화
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_memo, parent, false);
        }

        // 현재 Memo 객체 가져오기
        Memo currentMemo = memoList.get(position);

        // View 참조
        TextView titleView = convertView.findViewById(R.id.memoTitle);
        TextView bookView = convertView.findViewById(R.id.memoBook);
        TextView pageView = convertView.findViewById(R.id.memoBookPage);
        TextView createdAtView = convertView.findViewById(R.id.memoCreatedAt);
        TextView updatedAtView = convertView.findViewById(R.id.memoUpdatedAt);
        Button deleteButton = convertView.findViewById(R.id.btnDeleteMemo); // 삭제 버튼 참조

        // Memo 데이터 설정
        if (currentMemo != null) {
            titleView.setText("제목: " + currentMemo.getTitle());
            bookView.setText("책: " + currentMemo.getBookName());
            pageView.setText("페이지: " + currentMemo.getPageNumber());
            createdAtView.setText("생성시간: " + currentMemo.getCreatedAt());
            updatedAtView.setText("수정시간: " + currentMemo.getUpdatedAt());
        }

        // 삭제 버튼 클릭 이벤트
        deleteButton.setOnClickListener(v -> {
            if (context instanceof MemoActivity) {
                // MemoActivity의 deleteMemo 호출
                ((MemoActivity) context).deleteMemo(position);
            }
        });

        return convertView;
    }
}
