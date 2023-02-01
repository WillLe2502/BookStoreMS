package com.bookstore.admin.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherRestController {

	@Autowired
	private PublisherService service;

	@PostMapping("/publishers/check_unique")
	public String checkUnique(Integer id, String name) {
		return service.checkUnique(id, name);
	}
}
