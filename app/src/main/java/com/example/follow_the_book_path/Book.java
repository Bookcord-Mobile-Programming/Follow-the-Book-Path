package com.example.follow_the_book_path;
import java.util.Date;

public class Book {
    private String title;
    private String author;
    private Date date; // 날짜를 Date 객체로 저장
    private int imageResId;

    public Book(String title, String author, Date date, int imageResId) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public int getImageResId() {
        return imageResId;
    }
}
