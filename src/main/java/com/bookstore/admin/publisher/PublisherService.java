package com.bookstore.admin.publisher;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookstore.admin.entity.Publisher;
import com.bookstore.admin.exception.PublisherNotFoundException;

@Service
public class PublisherService {
	public static final int PUBLISHERS_PER_PAGE = 10;
	
	@Autowired
	private PublisherRepository repo;

	public List<Publisher> listAll() {
		return (List<Publisher>) repo.findAll();
	}
	
	public Page<Publisher> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PUBLISHERS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}

		return repo.findAll(pageable);		
	}
	
	public Publisher save(Publisher publisher) {
		return repo.save(publisher);
	}
	
	public Publisher get(Integer id) throws PublisherNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new PublisherNotFoundException("Could not find any publisher with ID " + id);
		}
	}

	public void delete(Integer id) throws PublisherNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new PublisherNotFoundException("Could not find any publisher with ID " + id);			
		}

		repo.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Publisher publisherByName = repo.findByName(name);

		if (isCreatingNew) {
			if (publisherByName != null) return "Duplicate";
		} else {
			if (publisherByName != null && publisherByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
}
