package com.example.follow_the_book_path;
import java.util.Date;

public class Book {
    private int bookId; // 책 ID
    private String title; // 책 제목
    private String author; // 저자
    private String genre; // 장르
    private Date startDate; // 읽기 시작 날짜
    private Date endDate; // 마지막으로 읽은 날짜
    private String status; // 읽은 상태
    private int imageResId; // 책 이미지

    public Book(int bookId, String title, String author, String genre, Date startDate, Date endDate, String status, int imageResId) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.imageResId = imageResId;
    }

    public int getBookId() { return bookId; }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }

    public String getStatus() {
        return status;
    }

    public int getImageResId() {
        return imageResId;
    }
}
