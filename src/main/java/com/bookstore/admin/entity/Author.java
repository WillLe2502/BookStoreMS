package com.bookstore.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "authors")
public class Author extends IdBasedEntity{

	@Column(nullable = false, length = 45, unique = true)
	private String name;

	@Column(nullable = false, length = 128)
	private String logo;
	
	@Column(length = 4096, nullable = false, name = "description")
	private String description;
	
	public Author() {

	}

	public Author(String name) {
		this.name = name;
		this.logo = "author-logo.png";
	}
	
	public Author(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Author [id=" + id + ", name=" + name + "]";
	}
	
	@Transient
	public String getLogoPath() {
		if (this.id == null) return "/images/image-thumbnail.png";

		return "/author-logos/" + this.id + "/" + this.logo;		
	}
	 	
}
