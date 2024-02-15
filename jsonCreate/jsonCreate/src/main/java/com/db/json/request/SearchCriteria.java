package com.db.json.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
   private String fieldName;
    private int age;
    private String value;
}
