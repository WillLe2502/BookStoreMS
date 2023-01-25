package com.bookstore.admin.publisher;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bookstore.admin.entity.Publisher;

public interface PublisherRepository extends PagingAndSortingRepository<Publisher, Integer> {

	public Long countById(Integer id);
	
	public Publisher findByName(String name);
	
	@Query("SELECT b FROM Publisher b WHERE b.name LIKE %?1%")
	public Page<Publisher> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT NEW Publisher(b.id, b.name) FROM Publisher b ORDER BY b.name ASC")
	public List<Publisher> findAll();
}
