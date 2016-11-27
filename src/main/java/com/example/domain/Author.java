package com.example.domain;

/**
 * Created by cate on 17/11/2016.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
public class Author extends AbstractPersistable<Long> {//come x i libri
        private String firstName;
        private String lastName;
        private Date birthDate;

        @JsonIgnore //l'insieme dei libri non è serializzato nel json
        @OneToMany(mappedBy = "author")//un autore può riferirsi a più libri..mappato su campo autore (unidirezionale)
        private Set<Book> books;

        public String getFirstName(){
                return this.firstName;
        }

        public void setFirstName(String firstName){
                this.firstName=firstName;
        }

        public String getLastName(){
                return this.lastName;
        }

        public void setLastName(String lastName){
                this.lastName=lastName;
        }

        public Date getBirthDate(){
                return this.birthDate;
        }

        public void setBirthDate(Date birthDate){ this.birthDate=birthDate; }

        public Set<Book> getBooks(){ return this.books; }

        public void setBooks(Set<Book> books){
                this.books=books;
        }
}