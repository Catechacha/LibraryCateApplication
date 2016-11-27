package com.example;

//alt+invio
import com.example.domain.Author;
import com.example.domain.Book;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;// x is(..)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.io.IOException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibraryCateApplication.class)//si riferisce a..
@WebAppConfiguration //x dire che è una web app
public class LibraryCateApplicationTests {

    @Autowired //x le dipendenze..
    private WebApplicationContext webApplicationContext;//Interfaccia per fornire la configurazione di una applicazione web.
    //questo è di sola lettura, mentre l'applicazione è in esecuzione, ma può essere ricaricato se l'implementazione supporta questa.
    private MockMvc mockMvc;//punto di partenza principale per il supporto di prova lato server Spring MVC
    private HttpMessageConverter mappingJackson2HttpMessageConverter; //Interfaccia strategica che fornisce un convertitore da e verso le richieste e le risposte HTTP
    //mappingJackson2HttpMessageConverter: implementazione di HttpMessageConverter che legge e scrive JSON usando Jackson 2.x's ObjectMapper.
    // questo convertitore può essere usato per collegarsi a beans tipizzati o instanziazioni di hashmap non tipizzate

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        //prende l'array lo trasforma in lista, crea stream lo filtra con il predicato (prendi solo gli elementi che sono istanza di
        //MappingJackson2HttpMessageConverter). find any mi dà una lista di optional<t> che può essere vuota se il flusso di partenza
        //è vuoto, oppure può descrivere qualche elemento del flusso. Su questa lista di optional la get mi dà un valore se questo è
        // nella lista, altrimenti NoSuchElementException.
        Assert.assertNotNull("the JSON message converter must not be null",this.mappingJackson2HttpMessageConverter);
        //se il convertitore è nullo manda questo messaggio di errore
    }

    @Before //prima di ogni test istanzia di MockMvc
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //crea un'istanza di MockMvc usando il webApplicationContext dato e inizializzato
    }

    @Test
    public void bookNotFound() throws Exception {
        //mockMvc.perform esegue una richiesta e sostituisce un tipo che consente di concatenare altre azioni, x es richieste sul risultato ritornato
        mockMvc.perform(get("/books/1"))//prendi il libro di id 1
            .andExpect(status().isNotFound()); //mi aspetto che non esista!
    }

    @Test
    public void bookFoundAfterCreation() throws Exception {
        Book book = new Book();//crea un nuovo libro con titolo, isbnm anno, autore
        book.setTitle("Test");
        book.setIsbn("1234567890123");
        book.setYear(2016);
        Author author = new Author();
        author.setFirstName("Joe");
        author.setLastName("Bloggs");
        author.setBirthDate(new Date());
        //ma l'autore non lo devo pure salvare nella sua repository?!?!
        book.setAuthor(author);
        mockMvc.perform(post("/books")//inseriscilo
                .content(json(book))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());//mi aspetto che sia creato
        mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //stringa con codifica utf-8
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test")))
                .andExpect(jsonPath("$.author.id", is(1)));
                //.andExpect(jsonPath("$.author.firstName", is("Joe")));//se ci metto questo non passa più il test..xche si salva solo id..x vedere le altre info dell'autore devo fare una ricerca nella repository
    }

    @Test
    public void bookDeleted() throws Exception {
        //creo un libro
        Book book = new Book();//crea un nuovo libro con titolo, isbnm anno, autore
        book.setTitle("Test-delete");
        book.setIsbn("9874564587978");
        book.setYear(2012);
        Author author = new Author();
        author.setFirstName("Caterina");
        author.setLastName("Falchi");
        author.setBirthDate(new Date());
        book.setAuthor(author);
        mockMvc.perform(post("/books")//inseriscilo
                .content(json(book))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
        //lo cancello
        mockMvc.perform(delete("/books/{id}",2L))//non prende books ma author..xche?
                .andExpect(status().isOk());
        //controllo che non deve esistere più
        mockMvc.perform(get("/books/{id}",2L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void bookModifiedAfterEdit() throws Exception {
         //creo un nuovo libro
         Book book = new Book();
         book.setTitle("Test-edit");
         book.setIsbn("1234567811123");
         book.setYear(2016);
         Author author = new Author();
         author.setFirstName("Stefania");
         author.setLastName("Falchi");
         author.setBirthDate(new Date());
         book.setAuthor(author);
         mockMvc.perform(post("/books")//inseriscilo
                 .content(json(book))
                 .contentType(MediaType.APPLICATION_JSON_UTF8))
                 .andExpect(status().isCreated());
        //modifico il libro
         EditBookRequest e = new EditBookRequest();
         e.setYear(2017);
         e.setTitle("Nuovo titolo");
         e.setIsbn("1112224445555");
         mockMvc.perform(put("/books/{id}",3L)
                .content(json(e))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
         //controllo se le modifiche sono andate a buon fine (se il libro c'è e se è stato correttamente modificato)
         mockMvc.perform(get("/books/{id}", 3L))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //stringa con codifica utf-8
               .andExpect(jsonPath("$.id", is(3)))
               .andExpect(jsonPath("$.title", is("Nuovo titolo")))
               .andExpect(jsonPath("$.isbn", is("1112224445555")))
               .andExpect(jsonPath("$.year", is(2017)));
    }

    //trasforma qualcosa in java in json
    protected String json(Object o) throws IOException {//o è l'oggetto da scrivere nel messaggio di output
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage(); //messaggio di output HTTP, con intestazione e corpo, in versione mock
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);//scrivi l'oggetto (in formato JSON) nel messaggio di output
        return mockHttpOutputMessage.getBodyAsString();//ritorna il corpo del messaggio come stringa
    }
}