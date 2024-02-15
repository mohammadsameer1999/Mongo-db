package com.db.json.status;

import com.db.json.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse <T>{
    private int status;
    private String message;
    private List<Person> doc = new ArrayList<>();



}
