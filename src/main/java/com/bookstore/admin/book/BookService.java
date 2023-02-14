package com.bookstore.admin.book;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookstore.admin.entity.book.Book;
import com.bookstore.admin.exception.BookNotFoundException;
import com.bookstore.admin.paging.PagingAndSortingHelper;

@Service
@Transactional
public class BookService {
	public static final int BOOKS_PER_PAGE = 5;


	@Autowired 
	private BookRepository repo;

	public List<Book> listAll() {
		return (List<Book>) repo.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId) {
		Pageable pageable = helper.createPageable(BOOKS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		Page<Book> page = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			} else {
				page = repo.findAll(keyword, pageable);
			}
		} else {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
			} else {		
				page = repo.findAll(pageable);
			}
		}
		
		helper.updateModelAttributes(pageNum, page);
	}
	
	public void searchBooks(int pageNum, PagingAndSortingHelper helper) {
		Pageable pageable = helper.createPageable(BOOKS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();		
		Page<Book> page = repo.searchBooksByName(keyword, pageable);		
		helper.updateModelAttributes(pageNum, page);
	}
	
	public Book save(Book book) {
		if (book.getId() == null) {
			book.setCreatedTime(new Date());
		}

		if (book.getAlias() == null || book.getAlias().isEmpty()) {
			String defaultAlias = book.getName().replaceAll(" ", "_");
			book.setAlias(defaultAlias);
		} else {
			book.setAlias(book.getAlias().replaceAll(" ", "_"));
		}
		
		if (book.getId() != null) {
			Book bookiInDb = repo.findById(book.getId()).get();
			book.setCreatedTime(bookiInDb.getCreatedTime());
		}
		
		book.setUpdatedTime(new Date());

		return repo.save(book);
	}
	
	public Book get(Integer id) throws BookNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BookNotFoundException("Could not find any book with ID " + id);
		}
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Book bookByName = repo.findByName(name);

		if (isCreatingNew) {
			if (bookByName != null) return "Duplicate";
		} else {
			if (bookByName != null && bookByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
	
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}
	
	public void delete(Integer id) throws BookNotFoundException {
		Long countById = repo.countById(null);
		
		if (countById == null || countById == 0) {
			throw new BookNotFoundException("Count not find any book with ID " + id);
			
		}
		
		repo.deleteById(id);
	}
	
	
}

