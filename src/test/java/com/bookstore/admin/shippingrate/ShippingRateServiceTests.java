package com.bookstore.admin.shippingrate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bookstore.admin.book.BookRepository;
import com.bookstore.admin.entity.ShippingRate;
import com.bookstore.admin.entity.book.Book;
import com.bookstore.admin.exception.ShippingRateNotFoundException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTests {

	@MockBean private ShippingRateRepository shipRepo;
	@MockBean private BookRepository bookRepo;

	@InjectMocks
	private ShippingRateService shipService;

	@Test
	public void testCalculateShippingCost_NoRateFound() {
		Integer productId = 1;
		Integer countryId = 234;
		String state = "ABCDE";

		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(null);

		assertThrows(ShippingRateNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				shipService.calculateShippingCost(productId, countryId, state);
			}
		});
	}

	@Test
	public void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException {
		Integer productId = 1;
		Integer countryId = 234;
		String state = "New York";

		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(10);

		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(shippingRate);

		Book book = new Book();
		book.setWeight(5);
		book.setWidth(4);
		book.setHeight(3);
		book.setLength(8);		

		Mockito.when(bookRepo.findById(productId)).thenReturn(Optional.of(book));

		float shippingCost = shipService.calculateShippingCost(productId, countryId, state);

		assertEquals(50, shippingCost);
	}
}