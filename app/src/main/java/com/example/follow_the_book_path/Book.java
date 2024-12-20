package com.example.follow_the_book_path;
import java.util.Date;

public class Book {
    private String title; //책제목
    private String author; //저자
    private String genre; // 장르
    private Date date; // 날짜를 Date 객체로 저장
    private String status; //읽은 상태
    private int imageResId; //책 이미지

    public Book(String title, String author, String genre, Date date, String status, int imageResId) {
        this.title = title;
        this.author = author;
        this.genre = this.genre;
        this.date = date;
        this.status = this.status; //읽은 상태
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getImageResId() {
        return imageResId;
    }
}
