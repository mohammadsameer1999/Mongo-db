package com.db.json;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class JsonCreateApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonCreateApplication.class, args);
	}

}
