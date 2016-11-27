package com.example;

/**
 * Created by cate on 19/11/2016.
 */

import java.util.Date;

public class AddBookRequest {
    private String title;
    private String isbn;
    private Integer year;
    private String authorFirstName;
    private String authorLastName;
    private Date authorBirthDate;

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

    public Integer getYear() {
        return this.year;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorFirstName() {
        return this.authorFirstName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorLastName() {
        return this.authorLastName;
    }

    public void setAuthorBirthDate(Date authorBirthDate) {
        this.authorBirthDate = authorBirthDate;
    }

    public Date getAuthorBirthDate() {
        return this.authorBirthDate;
    }
}