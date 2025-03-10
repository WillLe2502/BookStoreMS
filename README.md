![image](https://github.com/user-attachments/assets/f13acc82-0084-4505-a692-4ceb0fe575a8)# 🚀 Le Livre Book Store Inventory Management System (IMS)

The Inventory Management System (IMS) is a web-based application designed to help businesses manage their stock levels, track product sales.</br>
It provides a user-friendly interface for adding, updating, and monitoring inventory with real-time insights.

## 📚 Table of Contents
- Technologies
- Features

## 🎓Techonologies 
<!--
![frontend](https://img.shields.io/badge/Frontend-Java-239120) <br/>
![backend](https://img.shields.io/badge/Frontend-Java-239120) <br/>
![framework](https://img.shields.io/badge/Frontend-Java-239120) <br/>
![frontend](https://img.shields.io/badge/Frontend-Java-239120) <br/>
![frontend](https://img.shields.io/badge/Frontend-Java-239120) <br/>
![frontend](https://img.shields.io/badge/Frontend-Java-239120) <br/>
-->

- **Frontend:** Thymeleaf, Javascript
- **Backend:** Java
- **Framework:** Spring
- **Authentication:** OAuth 2.0
- **Database:** MySQL, AWS S3
- **Deployment:** Heroku


## ⭐ Features 
### 🔥 Login Page

> ![Image](https://github.com/user-attachments/assets/5b00bd62-66ff-45bd-95d1-0a58eaec5d1e)

### 🔥 Admin Page

> ![Image](https://github.com/user-attachments/assets/42710040-3ba0-479d-ae5d-42b8dd4da4ed1e)

Each Admin has a specific role. Each role will determine the accessibility to the web page.

Different between an Admin and Salesperson Role:

Admin Role

> ![Image](https://github.com/user-attachments/assets/2680b795-c469-4458-8df4-d597b405cde6)

Salesperson Role

> ![Image](https://github.com/user-attachments/assets/c8c1a0e2-3042-40cd-9e16-05053b122e70)

### 🔥 Product Managment

**View Product List** – Display all products with search and filter options.

> ![Image](https://github.com/user-attachments/assets/55ac2833-ed3f-43b6-bfc6-60fb5dd92bae)

**Add New Products** – add products by specifying details like name, category, price, stock quantity, and publisher.

> ![Image](https://github.com/user-attachments/assets/913c77b3-df03-4ca7-93f7-ca9381a8fcef)

**Edit/Update Products** – Modify existing product details such as price, quantity, and descriptions.

> ![Image](https://github.com/user-attachments/assets/9e8e705e-8449-4215-a99f-56523f1273e9)

**Database Schema** – Modify existing product details such as price, quantity, and descriptions.
- Book Table Schema
~~~~sql
CREATE TABLE books (
    id SERIAL PRIMARY KEY, -- Auto-incremented primary key

    cover VARCHAR(128) NOT NULL, 
    name VARCHAR(256) NOT NULL UNIQUE,
    alias VARCHAR(256) NOT NULL UNIQUE,
    isbn VARCHAR(256) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    
    enabled BOOLEAN DEFAULT TRUE, 
    in_stock BOOLEAN DEFAULT TRUE,

    cost DECIMAL(10,2) NOT NULL, 
    price DECIMAL(10,2) NOT NULL, 
    discount_percent DECIMAL(5,2) DEFAULT 0.00,

    length DECIMAL(10,2) DEFAULT 0.00,
    width DECIMAL(10,2) DEFAULT 0.00,
    height DECIMAL(10,2) DEFAULT 0.00,
    weight DECIMAL(10,2) DEFAULT 0.00,

    category_id INT,
    author_id INT,
    publisher_id INT,

    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE SET NULL,
    CONSTRAINT fk_publisher FOREIGN KEY (publisher_id) REFERENCES publishers(id) ON DELETE SET NULL);
~~~~

- Categories Table Schema
~~~~sql
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    
    name VARCHAR(128) NOT NULL UNIQUE,
    alias VARCHAR(64) NOT NULL UNIQUE,
    image VARCHAR(128) NOT NULL,
    
    enabled BOOLEAN DEFAULT TRUE,
    all_parent_ids VARCHAR(256) NULL,

    parent_id INT NULL,
    
    CONSTRAINT fk_parent_category FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL);
~~~~

- Author and Publisher Table Schema
~~~~sql
CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE);

CREATE TABLE publishers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE);
~~~~

### 🔥 Customer and Order Managment
**View Customer List** – Display all customers with search and filter options.

> ![Image](https://github.com/user-attachments/assets/f3f06d5f-8356-4102-a616-2abb97ad573e)

**Edit/Update Customer** – Modify existing customer details.

> ![Image](https://github.com/user-attachments/assets/b5a69a07-61e7-4fb6-b96f-5bf46a660ea9)

