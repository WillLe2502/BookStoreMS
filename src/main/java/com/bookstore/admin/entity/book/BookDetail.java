package com.bookstore.admin.entity.book;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bookstore.admin.entity.IdBasedEntity;

@Entity
@Table(name = "book_details")
public class BookDetail extends IdBasedEntity {

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 255)
	private String value;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	public BookDetail() {
	}

	public BookDetail(String name, String value, Book book) {
		this.name = name;
		this.value = value;
		this.book = book;
	}
	
	public BookDetail(Integer id, String name, String value, Book book) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.book = book;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
