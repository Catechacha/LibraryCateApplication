package com.example.controller;

/**
 * Created by cate on 19/11/2016.
 */

import com.example.AddBookRequest;
import com.example.EditBookRequest;
import com.example.exceptions.BookNotFoundException;
import com.example.service.BookService;
import com.example.domain.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //controller
@RequestMapping("/books") //per mappatura web di richieste su classi/handler specifici

public class BookController {
    @Autowired
    BookService bookService;

    //se un libro non c'è
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)//cambia lo status in not found
    public String handleAppException(BookNotFoundException ex) {
        return ex.getMessage();
    }

    //x prendere un libro di con id "id"
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Book getBook(@PathVariable("id") Long id) throws BookNotFoundException{
        return bookService.getBook(id);
    }

    //per creare un libro
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody AddBookRequest addBookRequest) {
        return bookService.createBook(
                addBookRequest.getTitle(),
                addBookRequest.getIsbn(),
                addBookRequest.getYear(),
                addBookRequest.getAuthorFirstName(),
                addBookRequest.getAuthorLastName(),
                addBookRequest.getAuthorBirthDate());
    }

    //lista di tutti i libri
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBooks(){
        return bookService.getBooks();
    }

    //x cancellare un libro
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("id") Long id)throws BookNotFoundException{
        bookService.deleteBook(id);
    }

    //per modificare un libro
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Book editBook(@PathVariable("id") Long id,@RequestBody EditBookRequest editBookRequest) throws BookNotFoundException{
        System.out.println(id+" "+editBookRequest.getTitle()+" "+editBookRequest.getIsbn()+" "+editBookRequest.getYear());
        return bookService.editBook(
                id,
                editBookRequest.getTitle(),
                editBookRequest.getIsbn(),
                editBookRequest.getYear()
        );
    }
}
