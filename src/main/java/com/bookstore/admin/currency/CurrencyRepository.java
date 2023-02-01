package com.bookstore.admin.currency;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.admin.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
	
	public List<Currency> findAllByOrderByNameAsc();
}