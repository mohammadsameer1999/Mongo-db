package com.db.json.controller;

import com.db.json.controller.service.PersonService;
import com.db.json.entity.Person;
import com.db.json.exception.ErrorResponse;
import com.db.json.exception.MissingFieldException;
import com.db.json.exception.PersonUpdateException;
import com.db.json.exception.ResourceNotFoundException;
import com.db.json.request.SearchRequest;
import com.db.json.status.CustomResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/hello")
public class PersonController {
    Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/addPerson")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person newPerson = personService.addPerson(person);
        return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
    }


    //    @PostMapping("/allPerson")
//    public ResponseEntity<CustomResponse<Person>> findByFirstName(@RequestParam String firstName) {
//        if (firstName.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//        try {
//            Person person = personService.findByFirstName(firstName);
//            if (person != null) {
//                CustomResponse<Person> response = new CustomResponse<>(200,"Success",person);
//                return ResponseEntity.ok(response);
//            } else {
//                CustomResponse<Person> response = new CustomResponse<>(400,"Not Found",null);
//                return ResponseEntity.ok(response);
//
//            }
//        } catch (Exception e) {
//            log.error("Error occurred while finding person by first name", e);
//            CustomResponse<Person> response = new CustomResponse<>(500,"Internal Server Error",null);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//    @PostMapping("/findByFirstName")
//    public ResponseEntity<CustomResponse<Person>> findByFirstName(@RequestBody Person request) {
//        String firstName = request.getFirstName();
//        Person person = personService.findByFirstName(firstName);
//        List<Person> personList = new ArrayList<>();
//        if (person != null) {
//            personList.add(person);
//            CustomResponse<Person> response = new CustomResponse<>(200, "Success", personList);
//            return ResponseEntity.ok(response);
//        } else {
//            CustomResponse<Person> response = new CustomResponse<>(404, "Not Found", null);
//            return ResponseEntity.ok(response);
//        }
//    }

    @PostMapping("/address")
    public ResponseEntity<CustomResponse<Person>> getPersonByAddress(@RequestBody Person request) {
        String address = request.getAddress();
        Person person = personService.getPersonByAddress(address);
        if (person != null) {
            List<Person> personList = new ArrayList<>();
            personList.add(person);
            CustomResponse<Person> response = new CustomResponse<>(200, "Success", personList);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<Person> response = new CustomResponse<>(404, "Not Found", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<ErrorResponse<Person>> searchPerson(@RequestBody SearchRequest request) {
        try {
            String fieldName = request.getSearch().getFieldName();
            String value = request.getSearch().getValue();
            int page = request.getPage();
            int limit = request.getLimit();

            // Validate the request parameters
            if (fieldName == null || page <= 0 || limit <= 0) {
                throw new MissingFieldException("Invalid search request parameters");
            }
            List<Person> personList = personService.findByFirstName(fieldName, value, page, limit);

            ErrorResponse successResponse = new ErrorResponse(HttpStatus.OK.value(), "Success", personList);
            return ResponseEntity.ok(successResponse);
        } catch (MissingFieldException e) {
            // Handle MissingFieldException
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (ResourceNotFoundException e) {
            // Handle ResourceNotFoundException
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Handle other exceptions
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/person/{id}")
    public ResponseEntity<CustomResponse<Person>> updatePerson(@RequestBody Person person, @PathVariable String id) {
        Person oldPerson = personService.updatePerson(person, id);
        log.info("old Person {}", oldPerson);
        if (oldPerson != null) {
            List<Person> personList = new ArrayList<>();
            personList.add(oldPerson);
            CustomResponse<Person> response = new CustomResponse<>(200, "Success", personList);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<Person> response = new CustomResponse<>(404, "Not Found", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Person>> deletedPerson(@PathVariable String id) {
        boolean updated = personService.deletedPerson(id);
        if (updated) {
            CustomResponse<Person> response = new CustomResponse<>(200,"Success",null);
            return ResponseEntity.ok(response);
        } else {
            CustomResponse<Person> response = new CustomResponse<>(404,"Not Found",null);
            return ResponseEntity.ok(response);
        }
    }

    //UpdatedPerson
    @PostMapping("/updated/{id}")
    public ResponseEntity<ErrorResponse<Person>> updatedPerson(@RequestBody Person person,@PathVariable String id) {
        try {
            Person person1 = personService.updatedPerson(person,id);
            log.info("search Person {}",person1);

            ErrorResponse<Person> response = new ErrorResponse<>(200,"Person Updated SuccessFully", null);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }catch (PersonUpdateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
