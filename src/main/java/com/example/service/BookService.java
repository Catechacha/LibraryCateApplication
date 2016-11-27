package com.example.service;

/**
 * Created by cate on 19/11/2016.
 */

import com.example.domain.Author;
import com.example.domain.Book;
import com.example.exceptions.BookNotFoundException;
import com.example.repository.BookRepository;
import com.example.repository.AuthorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service//offre servizi e basta, non ha uno stato
@Transactional(rollbackFor = Throwable.class)//descrivi gli attributi di transazione su un metodo o classe.
//rollback definisce le classi di eccezioni da usare in caso di errori

public class BookService{

    //definisco una "tabella" di autori e una di libri
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    public Book getBook(Long id) throws BookNotFoundException {
        Book book = bookRepository.findOne(id);//trova nella tabella il libro tramite l'id (findOne non l'ho implementato io!)
        if(book == null)
            throw new BookNotFoundException();
        return book;
    }

    public Book createBook(String title, String isbn, Integer year, String authorFirstName, String
            authorLastName, Date authorBirthDate) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setYear(year);
        Author author = authorRepository.findByFirstNameAndLastNameAndBirthDate(authorFirstName, authorLastName,
                authorBirthDate);//guarda se l autore esiste gia o no
        if(author == null) {
            author = new Author();
            author.setFirstName(authorFirstName);
            author.setLastName(authorLastName);
            author.setBirthDate(authorBirthDate);
            //non manca authorReposiroty.save(author)??
        }
        book.setAuthor(author);
        return bookRepository.save(book);//salva in libro nella tabella dei libri..è qui che salvo in automatico l'autore nella repository degli autori?!
    }

    //lista di tutti i libri
    public List<Book> getBooks(){
        List<Book> listBook = bookRepository.findAll();
        return listBook;
    }

    //cancella un libro
    public void deleteBook(Long id) throws BookNotFoundException{//forse deve ritornare qualcosa?!..
        Book book = bookRepository.findOne(id);//trova nella tabella il libro tramite l'id
        if(book == null)
            throw new BookNotFoundException();
        bookRepository.delete(book.getId());
    }

    //modifica un libro
    public Book editBook(Long id, String title, String isbn, Integer year) throws BookNotFoundException{
        Book book = bookRepository.findOne(id);//trova nella tabella il libro tramite l'id
        if(book == null)
            throw new BookNotFoundException();
        book.setTitle(title);
        book.setYear(year);
        book.setIsbn(isbn);
        return book;
    }
}