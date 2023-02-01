package com.bookstore.admin.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.admin.entity.Country;
import com.bookstore.admin.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {

	public List<State> findByCountryOrderByNameAsc(Country country);
}