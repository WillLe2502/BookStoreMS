package com.bookstore.admin.publisher;

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
import com.bookstore.admin.entity.Publisher;
import com.bookstore.admin.exception.PublisherNotFoundException;

@Controller
public class PublisherController {

	@Autowired
	private PublisherService service;
	
	private String defaultRedirectURL = "redirect:/publishers/page/1?sortField=id&sortDir=asc";

	@GetMapping("/publishers")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/publishers/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword
			) {
		Page<Publisher> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<Publisher> listPublishers = page.getContent();

		long startCount = (pageNum - 1) * service.PUBLISHERS_PER_PAGE + 1;
		long endCount = startCount + service.PUBLISHERS_PER_PAGE - 1;
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
		model.addAttribute("listPublishers", listPublishers);
		model.addAttribute("moduleURL", "/publishers");

		return "publishers/publishers";
	}
	
	@GetMapping("/publishers/new")
	public String newPublisher(Model model) {
		model.addAttribute("publisher", new Publisher());
		model.addAttribute("pageTitle", "Create New Publisher");

		return "publishers/publisher_form";		
	}
	
	@PostMapping("/publishers/save")
	public String savePublisher(
				Publisher publisher, 
				@RequestParam("fileImage") MultipartFile multipartFile,
				RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			publisher.setLogo(fileName);

			Publisher savedPublisher = service.save(publisher);
			String uploadDir = "../publisher-logos/" + savedPublisher.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			service.save(publisher);
		}

		ra.addFlashAttribute("message", "The publisher has been saved successfully.");
		return "redirect:/publishers";		
	}
	
	@GetMapping("/publishers/edit/{id}")
	public String editPublisher(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Publisher publisher = service.get(id);
			
			model.addAttribute("publisher", publisher);
			model.addAttribute("pageTitle", "Edit Publisher (ID: " + id + ")");

			return "publishers/publisher_form";			
		} catch (PublisherNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/publishers";
		}
	}
	
	@GetMapping("/publishers/delete/{id}")
	public String deletePublisher(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String publisherDir = "../publisher-logos/" + id;
			FileUploadUtil.removeDir(publisherDir);

			redirectAttributes.addFlashAttribute("message", 
					"The publisher ID " + id + " has been deleted successfully");
		} catch (PublisherNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/publishers";
	}	
	
	
}
