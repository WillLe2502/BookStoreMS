package com.bookstore.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.bookstore.admin.entity"})
public class BookStoreMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreMsApplication.class, args);
	}

}
