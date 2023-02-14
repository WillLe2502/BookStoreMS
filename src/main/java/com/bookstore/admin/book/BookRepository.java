package com.bookstore.admin.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.admin.entity.book.Book;
import com.bookstore.admin.paging.SearchRepository;

public interface BookRepository extends SearchRepository<Book, Integer> {
	public Book findByName(String name);
	
	@Query("UPDATE Book b SET b.enabled = ?2 WHERE b.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);	
	
	public Long countById(Integer id);
	
	@Query("SELECT b FROM Book b WHERE b.name LIKE %?1% " 
			+ "OR b.description LIKE %?1% "
			+ "OR b.publisher.name LIKE %?1% "
			+ "OR b.author.name LIKE %?1% "
			+ "OR b.category.name LIKE %?1%")
	public Page<Book> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.category.id = ?1 "
			+ "OR b.category.allParentIDs LIKE %?2%")	
	public Page<Book> findAllInCategory(Integer categoryId, String categoryIdMatch, 
			Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE (b.category.id = ?1 "
			+ "OR b.category.allParentIDs LIKE %?2%) AND "
			+ "(b.name LIKE %?3% " 
			+ "OR b.description LIKE %?3% "
			+ "OR b.publisher.name LIKE %?3% "
			+ "OR b.author.name LIKE %?3% "
			+ "OR b.category.name LIKE %?3%)")			
	public Page<Book> searchInCategory(Integer categoryId, String categoryIdMatch, 
			String keyword, Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.name LIKE %?1%")
	public Page<Book> searchBooksByName(String keyword, Pageable pageable);

}
