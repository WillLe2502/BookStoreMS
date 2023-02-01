package com.bookstore.admin.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorRestController {
	@Autowired
	private AuthorService service;

	@PostMapping("/authors/check_unique")
	public String checkUnique(Integer id, String name) {
		return service.checkUnique(id, name);
	}
}
