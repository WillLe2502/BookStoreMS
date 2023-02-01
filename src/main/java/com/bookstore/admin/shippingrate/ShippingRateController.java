package com.bookstore.admin.shippingrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.admin.entity.Country;
import com.bookstore.admin.entity.Publisher;
import com.bookstore.admin.entity.ShippingRate;
import com.bookstore.admin.exception.ShippingRateAlreadyExistsException;
import com.bookstore.admin.exception.ShippingRateNotFoundException;
import com.bookstore.admin.paging.PagingAndSortingHelper;
import com.bookstore.admin.paging.PagingAndSortingParam;

@Controller
public class ShippingRateController {
	private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";

	@Autowired private ShippingRateService service;

	@GetMapping("/shipping_rates")
	public String listFirstPage() {
		return defaultRedirectURL;
	}

	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(
			@Param("sortField") String sortField, 
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword, 
			Model model,
			@PathVariable(name = "pageNum") int pageNum) {
		
		Page<ShippingRate> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<ShippingRate> listShippingRates = page.getContent();

		long startCount = (pageNum - 1) * service.RATES_PER_PAGE + 1;
		long endCount = startCount + service.RATES_PER_PAGE - 1;
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
		model.addAttribute("listShippingRates", listShippingRates);
				model.addAttribute("moduleURL", "/shipping_rates");
				
		return "shipping_rates/shipping_rates";
	}	

	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {
		List<Country> listCountries = service.listAllCountries();

		model.addAttribute("rate", new ShippingRate());
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "New Rate");
		
		return "shipping_rates/shipping_rate_form";		
	}

	@PostMapping("/shipping_rates/save")
	public String saveRate(ShippingRate rate, RedirectAttributes ra) {
		try {
			service.save(rate);
			ra.addFlashAttribute("message", "The shipping rate has been saved successfully.");
		} catch (ShippingRateAlreadyExistsException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/shipping_rates/edit/{id}")
	public String editRate(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes ra) {
		try {
			ShippingRate rate = service.get(id);
			List<Country> listCountries = service.listAllCountries();

			model.addAttribute("listCountries", listCountries);			
			model.addAttribute("rate", rate);
			model.addAttribute("pageTitle", "Edit Rate (ID: " + id + ")");

			return "shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCODSupport(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "supported") Boolean supported,
			Model model, RedirectAttributes ra) {
		try {
			service.updateCODSupport(id, supported);
			ra.addFlashAttribute("message", "COD support for shipping rate ID " + id + " has been updated.");
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteRate(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes ra) {
		try {
			service.delete(id);
			ra.addFlashAttribute("message", "The shipping rate ID " + id + " has been deleted.");
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}	
}