package com.db.json.repository;

import com.db.json.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person,String> {
    Person findByFirstName(String firstName);
//    Page<Person> findByFirstName(String firstName, Pageable pageable);

    List<Person> findByFirstNameAndLastName(String firstName, String lastName);

    List<Person> findByFirstName(String firstName, int page, int limit);

    List<Person> findByFirstName(String firstName, PageRequest pageRequest);
}
