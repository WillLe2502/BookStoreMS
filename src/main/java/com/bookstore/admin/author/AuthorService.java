package com.bookstore.admin.author;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookstore.admin.entity.Author;
import com.bookstore.admin.exception.AuthorNotFoundException;

@Service
public class AuthorService {
	public static final int AUTHORS_PER_PAGE = 10;
	
	@Autowired
	private AuthorRepository repo;

	public List<Author> listAll() {
		return (List<Author>) repo.findAll();
	}
	
	public Page<Author> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, AUTHORS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}

		return repo.findAll(pageable);		
	}
	
	public Author save(Author author) {
		return repo.save(author);
	}
	
	public Author get(Integer id) throws AuthorNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new AuthorNotFoundException("Could not find any author with ID " + id);
		}
	}

	public void delete(Integer id) throws AuthorNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new AuthorNotFoundException("Could not find any author with ID " + id);			
		}

		repo.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Author authorByName = repo.findByName(name);

		if (isCreatingNew) {
			if (authorByName != null) return "Duplicate";
		} else {
			if (authorByName != null && authorByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
}
