package com.example.mongospringboottest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MongoSpringbootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoSpringbootTestApplication.class, args);
	}

}
