package com.example;

/**
 * Created by cate on 19/11/2016.
 */

public class EditBookRequest {
    private String title;
    private String isbn;
    private Integer year;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() { return this.year; }
}