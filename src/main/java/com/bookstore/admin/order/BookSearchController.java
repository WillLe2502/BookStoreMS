package com.bookstore.admin.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bookstore.admin.book.BookService;
import com.bookstore.admin.paging.PagingAndSortingHelper;
import com.bookstore.admin.paging.PagingAndSortingParam;

@Controller
public class BookSearchController {

	@Autowired private BookService service;

	@GetMapping("/orders/search_book")
	public String showSearchBookPage() {
		return "orders/search_book";
	}

	@PostMapping("/orders/search_book")
	public String searchBooks(String keyword) {
		return "redirect:/orders/search_book/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}

	@GetMapping("/orders/search_book/page/{pageNum}")
	public String searchBooksByPage(@PagingAndSortingParam(
			listName = "listBooks", 
			moduleURL = "/orders/search_book") PagingAndSortingHelper helper,
			Model model,
			@Param("sortField") String sortField, 
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@PathVariable(name = "pageNum") int pageNum) {
		service.searchBooks(pageNum, helper);
		return "orders/search_book";
	}
}