package com.bookstore.admin.author;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import com.bookstore.admin.entity.Author;
import com.bookstore.admin.exception.AuthorNotFoundException;
import com.bookstore.admin.exception.PublisherNotFoundException;

@Controller
public class AuthorController {
	
	@Autowired
	private AuthorService service;
	
	@GetMapping("/authors")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "id", "asc", null);
	}
	
	@GetMapping("/authors/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword
			) {
		Page<Author> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Author> listAuthors = page.getContent();

		long startCount = (pageNum - 1) * service.AUTHORS_PER_PAGE + 1;
		long endCount = startCount + service.AUTHORS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);		
		model.addAttribute("listAuthors", listAuthors);

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
			String uploadDir = "../author-logos/" + savedAuthor.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

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
			String authorDir = "../publisher-logos/" + id;
			FileUploadUtil.removeDir(authorDir);

			redirectAttributes.addFlashAttribute("message", 
					"The author ID " + id + " has been deleted successfully");
		} catch (AuthorNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/authors";
	}	
	
	
}
