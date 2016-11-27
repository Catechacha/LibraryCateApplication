package com.example.repository;

import com.example.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by cate on 19/11/2016.
 */

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>,JpaSpecificationExecutor<Author> {
    Author findByFirstNameAndLastNameAndBirthDate(String authorFirstName, String authorLastName, Date authorBirthDate);
}