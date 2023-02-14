package com.bookstore.admin.author;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.admin.entity.Author;
import com.bookstore.admin.paging.SearchRepository;

public interface AuthorRepository extends SearchRepository<Author, Integer> {

	public Long countById(Integer id);
	
	public Author findByName(String name);
	
	@Query("SELECT b FROM Author b WHERE b.name LIKE %?1%")
	public Page<Author> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT a FROM Author a ORDER BY a.name ASC")
	public List<Author> findAll();
}
