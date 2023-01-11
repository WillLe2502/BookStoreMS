package com.bookstore.admin.publisher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.bookstore.admin.entity.Publisher;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class PublisherRepositoryTests {
	
	@Autowired
	private PublisherRepository repo;

	@Test
	public void testCreatePublisher1() {
		Publisher publiser = new Publisher("Pan Macmillan");

		Publisher savedPublisher = repo.save(publiser);

		assertThat(savedPublisher).isNotNull();
		assertThat(savedPublisher.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreatePublisher2() {
		Publisher publiser = new Publisher("Penguin");

		Publisher savedPublisher = repo.save(publiser);

		assertThat(savedPublisher).isNotNull();
		assertThat(savedPublisher.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrand3() {
		Publisher publiser = new Publisher("HarperCollins Publishers");

		Publisher savedPublisher = repo.save(publiser);

		assertThat(savedPublisher).isNotNull();
		assertThat(savedPublisher.getId()).isGreaterThan(0);
	}

	@Test
	public void testFindAll() {
		Iterable<Publisher> publisers = repo.findAll();
		publisers.forEach(System.out::println);

		assertThat(publisers).isNotEmpty();
	}

	@Test
	public void testGetById() {
		Publisher publiser = repo.findById(1).get();

		assertThat(publiser.getName()).isEqualTo("Pan Macmillan");
	}

	@Test
	public void testUpdateName() {
		String newName = "Head of Zeus";
		Publisher publiser = repo.findById(3).get();
		publiser.setName(newName);

		Publisher savedPublisher = repo.save(publiser);
		assertThat(savedPublisher.getName()).isEqualTo(newName);
	}

	@Test
	public void testDelete() {
		Integer id = 2;
		repo.deleteById(id);

		Optional<Publisher
		> result = repo.findById(id);

		assertThat(result.isEmpty());
	}

}
