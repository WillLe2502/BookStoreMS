package com.bookstore.admin.author;

import java.io.IOException;

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

import com.bookstore.admin.AmazonS3Util;
import com.bookstore.admin.FileUploadUtil;
import com.bookstore.admin.entity.Author;
import com.bookstore.admin.exception.AuthorNotFoundException;
import com.bookstore.admin.paging.PagingAndSortingHelper;
import com.bookstore.admin.paging.PagingAndSortingParam;

@Controller
public class AuthorController {
	
	private String defaultRedirectURL = "redirect:/authors/page/1?sortField=id&sortDir=asc";
	
	@Autowired
	private AuthorService service;
	
	@GetMapping("/authors")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/authors/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listAuthors", moduleURL = "/authors") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum
			) {
		service.listByPage(pageNum, helper);

		return "authors/authors";
	}
	
	@GetMapping("/authors/new")
	public String newAuthor(Model model) {
		model.addAttribute("author", new Author());
		model.addAttribute("pageTitle", "Create New Authorr");

		return "authors/author_form";		
	}
	
	@PostMapping("/authors/save")
	public String savePublisher(
				Author author, 
				@RequestParam("fileImage") MultipartFile multipartFile,
				RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			author.setLogo(fileName);

			Author savedAuthor = service.save(author);
			String uploadDir = "author-logos/" + savedAuthor.getId();

//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
		} else {
			service.save(author);
		}

		ra.addFlashAttribute("message", "The author has been saved successfully.");
		return "redirect:/authors";		
	}
	
	@GetMapping("/authors/edit/{id}")
	public String editPublisher(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Author author = service.get(id);
			
			model.addAttribute("author", author);
			model.addAttribute("pageTitle", "Edit Author (ID: " + id + ")");

			return "authors/author_form";
		} catch (AuthorNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/authors";
		}
	}
	
	@GetMapping("/authors/delete/{id}")
	public String deletePublisher(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String authorDir = "author-logos/" + id;
			AmazonS3Util.removeFolder(authorDir);

			redirectAttributes.addFlashAttribute("message", 
					"The author ID " + id + " has been deleted successfully");
		} catch (AuthorNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/authors";
	}	
	
	
}
