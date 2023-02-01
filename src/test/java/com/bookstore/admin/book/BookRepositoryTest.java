package com.bookstore.admin.book;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.bookstore.admin.entity.Author;
import com.bookstore.admin.entity.Category;
import com.bookstore.admin.entity.Publisher;
import com.bookstore.admin.entity.book.Book;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BookRepositoryTest {

	@Autowired
	private BookRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateBook() {
		Publisher publisher = entityManager.find(Publisher.class, 37);
		Category category = entityManager.find(Category.class, 52);

		Book book = new Book();
		book.setName("Loathe To Love You");
		book.setIsbn("9781408726778");
		book.setDescription("Description for Loathe To Love You");

		book.setPublisher(publisher);
		book.setCategory(category);

		book.setPrice(18);
		book.setCost(15);
		book.setEnabled(true);
		book.setInStock(true);

		book.setCreatedTime(new Date());
		book.setUpdatedTime(new Date());

		Book savedbook = repo.save(book);

		assertThat(savedbook).isNotNull();
		assertThat(savedbook.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBook2() {
		Publisher publisher = entityManager.find(Publisher.class, 5);
		Author author = entityManager.find(Author.class, 61);
		Category category = entityManager.find(Category.class, 51);

		Book book = new Book();
		book.setName("Alone With You In The Ether");
		book.setIsbn("9781035012916");
		book.setDescription("Description for Alone With You In The Ether");

		book.setPublisher(publisher);
		book.setAuthor(author);
		book.setCategory(category);

		book.setPrice(18);
		book.setCost(15);
		book.setEnabled(true);
		book.setInStock(true);

		book.setCreatedTime(new Date());
		book.setUpdatedTime(new Date());

		Book savedbook = repo.save(book);

		assertThat(savedbook).isNotNull();
		assertThat(savedbook.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllBooks() {
		Iterable<Book> iterableBooks = repo.findAll();

		iterableBooks.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 2;
		Book book = repo.findById(id).get();
		System.out.println(book);

		assertThat(book).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Book book = repo.findById(id).get();
		book.setPrice(29);

		repo.save(book);

		Book updatedBook = entityManager.find(Book.class, id);

		assertThat(updatedBook.getPrice()).isEqualTo(30);
	}
	
	@Test
	public void testSaveBookWithDetails() {
		Integer bookId = 1;
		Book book = repo.findById(bookId).get();

		book.addDetail("Subtitle", "From the bestselling author of The Love Hypothesis");
		book.addDetail("Format", "PaperBack");
		book.addDetail("Pages", "384");

		Book savedBook = repo.save(book);
		assertThat(savedBook.getDetails()).isNotEmpty();		
	}
}
