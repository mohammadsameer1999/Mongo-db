package com.db.json.controller.service;

import com.db.json.entity.Person;
import com.db.json.exception.MissingFieldException;
import com.db.json.exception.PersonUpdateException;
import com.db.json.exception.ResourceNotFoundException;
import com.db.json.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PersonRepository personRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person addPerson(Person person) {
        log.info("save Person for db {} :", person);
        Person newPerson = personRepository.save(person);
        return newPerson;
    }

    @Override
    public Person getAllPerson(String firstName) {
        log.info("save find Person in db {}", firstName);
        Person person1 = personRepository.findByFirstName(firstName);
        log.info("saved db {} :", person1);
        return person1;
    }

    //
//    @Override
//    public Person findByFirstName(String firstName) {
//        log.info("save find Person in db {}", firstName);
//
//        return personRepository.findByFirstName(firstName);
//    }
//    @Override
//    public Person findByFirstName(String firstName) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("firstName").is(firstName));
//        return mongoTemplate.findOne(query, Person.class);
//    }

    @Override
    public Person getPersonByAddress(String address) {
        Query query = new Query();
        query.addCriteria(Criteria.where("address").is(address));
        return mongoTemplate.findOne(query, Person.class);
    }

    @Override
    public List<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        log.info("find first & last {} {} ", firstName, lastName);
        return personRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public Person updatePerson(Person person, String id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        log.info("optinal person {}", optionalPerson);
        if (optionalPerson.isPresent()) {
            Person existPerson = optionalPerson.get();
            existPerson.setLastName(person.getLastName());
            existPerson.setAddress(person.getAddress());
            log.info("exist Person {}", existPerson);
            return personRepository.save(existPerson);
        } else {
            return null;
        }
    }

    @Override
    public boolean deletedPerson(String id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            personRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Person> findByFirstName(String fieldName, String value, int page, int limit) {
        try {
            Criteria criteria = Criteria.where(fieldName).is(value);
            log.info("criteria {}",criteria);
            Pageable pageable = PageRequest.of( page - 1,limit);
            log.info("paggable {}",pageable);
            Query query = new Query(criteria).with(pageable);
            log.info("query {}",query);
            List<Person> personList =  mongoTemplate.find(query,Person.class);
            if (personList.isEmpty()) {
                throw new ResourceNotFoundException("Person not found with criteria");
            }
            else {
                return personList;
            }
        } catch (Exception e) {
            throw new MissingFieldException("An error occurred while fetching data from database");        }
    }

    @Override
    public Person updatedPerson(Person person, String id)  throws ResourceNotFoundException, PersonUpdateException {
        Query query = new Query(Criteria.where("id").is(id));
        Person existingPerson = mongoTemplate.findOne(query,Person.class);
        log.info("person is aviable {} {}",existingPerson,id);
        if (existingPerson == null) {
            throw new ResourceNotFoundException("Person not found with id: " + id);
        }
        existingPerson.setFirstName(person.getFirstName());
        try {
           return mongoTemplate.save(existingPerson);
        } catch (Exception e) {
            throw new PersonUpdateException("Failed to update Person with id :" + id);
        }
    }
}