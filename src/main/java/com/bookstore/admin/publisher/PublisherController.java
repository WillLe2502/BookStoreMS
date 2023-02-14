package com.bookstore.admin.publisher;

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
import com.bookstore.admin.entity.Publisher;
import com.bookstore.admin.exception.PublisherNotFoundException;
import com.bookstore.admin.paging.PagingAndSortingHelper;
import com.bookstore.admin.paging.PagingAndSortingParam;

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
			@PagingAndSortingParam(listName = "listPublishers", moduleURL = "/publishers") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum
			) {
		service.listByPage(pageNum, helper);
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
			String uploadDir = "publisher-logos/" + savedPublisher.getId();

			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
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
			String uploadDir = "publisher-logos/" + id;
			AmazonS3Util.removeFolder(uploadDir);

			redirectAttributes.addFlashAttribute("message", 
					"The publisher ID " + id + " has been deleted successfully");
		} catch (PublisherNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/publishers";
	}	
	
	
}
