package com.bookstore.admin.book;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.admin.FileUploadUtil;
import com.bookstore.admin.author.AuthorService;
import com.bookstore.admin.category.CategoryService;
import com.bookstore.admin.entity.Author;
import com.bookstore.admin.entity.Category;
import com.bookstore.admin.entity.Publisher;
import com.bookstore.admin.entity.book.Book;
import com.bookstore.admin.exception.BookNotFoundException;
import com.bookstore.admin.paging.PagingAndSortingHelper;
import com.bookstore.admin.paging.PagingAndSortingParam;
import com.bookstore.admin.publisher.PublisherService;

@Controller
public class BookController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

	@Autowired private BookService bookService;
	@Autowired private PublisherService publisherService;
	@Autowired private AuthorService authorService;
	@Autowired private CategoryService categoryService;
	
	private String defaultRedirectURL = "redirect:/books/page/1?sortField=id&sortDir=asc&categoryId=0";
	
	@GetMapping("/books")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/books/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBooks", moduleURL = "/books") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,
			Integer categoryId,
			Model model
			) {
		bookService.listByPage(pageNum, helper, categoryId);
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		if (categoryId != null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);
		return "books/books";		
	}
	
	@GetMapping("/books/new")
	public String newBook(Model model) {
		List<Publisher> listPublishers = publisherService.listAll();
		List<Author> listAuthors = authorService.listAll();
		List<Category> listCategories = categoryService.listAll();

		Book book = new Book();
		book.setEnabled(true);
		book.setInStock(true);

		model.addAttribute("book", book);
		model.addAttribute("listPublishers", listPublishers);
		model.addAttribute("listAuthors", listAuthors);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Book");

		return "books/book_form";
	}

	@PostMapping("/books/save")
	public String saveBook(Book book, 
			RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile multipartFile,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			book.setCover(fileName);
			setBookDetails(detailIDs, detailNames, detailValues, book);

			Book savedBook = bookService.save(book);
			
			String uploadDir = "../book-covers/" + savedBook.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			setBookDetails(detailIDs, detailNames, detailValues, book);
			bookService.save(book);
		}
		
		ra.addFlashAttribute("message", "The book has been saved successfully.");

		return "redirect:/books";
	}
	
	private void setBookDetails(String[] detailIDs, String[] detailNames, 
			String[] detailValues, Book book) {
		if (detailNames == null || detailNames.length == 0) return;
		
		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);
			
			if (id != 0) {
				book.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				book.addDetail(name, value);
			}
		}
	}
	
	@GetMapping("/books/{id}/enabled/{status}")
	public String updateBookEnabledStatus(
			@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, 
			RedirectAttributes redirectAttributes) {
		bookService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Book ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/books";
	}
	
	@GetMapping("/books/edit/{id}")
	public String editPublisher(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Book book = bookService.get(id);
			List<Publisher> listPublishers = publisherService.listAll();
			List<Author> listAuthors = authorService.listAll();
			List<Category> listCategories = categoryService.listAll();
			
			model.addAttribute("book", book);
			model.addAttribute("pageTitle", "Edit Book (ID: " + id + ")");
			model.addAttribute("listPublishers", listPublishers);
			model.addAttribute("listAuthors", listAuthors);
			model.addAttribute("listCategories", listCategories);

			return "books/book_form";
		} catch (BookNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/books";
		}
	}
	
	@GetMapping("/books/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Book book = bookService.get(id);			
			model.addAttribute("book", book);		

			return "books/book_detail_modal";

		} catch (BookNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/books";
		}
	}
	
}
