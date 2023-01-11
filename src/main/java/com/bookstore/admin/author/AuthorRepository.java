package com.bookstore.admin.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bookstore.admin.entity.Author;

public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {

	public Long countById(Integer id);
	
	public Author findByName(String name);
	
	@Query("SELECT b FROM Author b WHERE b.name LIKE %?1%")
	public Page<Author> findAll(String keyword, Pageable pageable);
}
