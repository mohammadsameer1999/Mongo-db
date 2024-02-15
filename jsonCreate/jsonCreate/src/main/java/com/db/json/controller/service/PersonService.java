package com.db.json.controller.service;

import com.db.json.entity.Person;

import java.util.List;

public interface PersonService {
    Person addPerson(Person person);

    Person getAllPerson(String firstName);


//    Person findByFirstName(String firstName);


    Person getPersonByAddress(String address);


    List<Person> findByFirstNameAndLastName(String firstName, String lastName);

    Person updatePerson(Person person, String id);

    boolean deletedPerson(String id);

    List<Person> findByFirstName(String fieldName, String value, int page, int limit);

    Person updatedPerson(Person person, String id);
}