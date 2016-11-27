package com.example.domain;

/**
 * Created by cate on 17/11/2016.
 */

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Entity //x dire che è un'entità ..
public class Book extends AbstractPersistable<Long> {
    //abstractPersistable è una classe astratta di base per le entità, permette la parametrizzazione dell'ID in questo caso(<long>)
    private String title;

    @Length(min = 13, max = 13)//lunghezza compresa tra
    @Column(unique = true)//deve essere unico
    private String isbn;

    private Integer year;

    public String getTitle(){
        return this.title;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})//più libri possono riferirsi al solito autore; le operazioni sull'autore sono propagate a tutti i libri che ha scritto
    private Author author;

    public void setTitle(String title){
        this.title=title;
    }

    public String getIsbn(){
        return this.isbn;
    }

    public void setIsbn(String isbn){
        this.isbn=isbn;
    }

    public Integer getYear(){
        return this.year;
    }

    public void setYear(Integer year){ this.year=year; }

    public Author getAuthor(){ return this.author; }

    public void setAuthor(Author author){ this.author=author; }
}
