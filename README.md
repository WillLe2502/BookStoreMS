# 🚀 Le Livre Book Store Inventory Management System (IMS)

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

**Database Schema** 
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

**View/Manage Order Details**
> ![Image](https://github.com/user-attachments/assets/f7517900-6003-476a-aee7-473d9253b2ef)
> ![Image](https://github.com/user-attachments/assets/9f505e42-ab8b-4210-886f-15788cef004e)
> ![Image](https://github.com/user-attachments/assets/f32df5e9-4a91-486e-ad52-4ba6ec5f4571)

**Database Schema** 
- Customer Table scheme
~~~~sql
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,

    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL,
    
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    
    address_line_1 VARCHAR(64) NULL,
    address_line_2 VARCHAR(64) NULL,
    
    city VARCHAR(45) NOT NULL,
    state VARCHAR(45) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,

    country_id INT NOT NULL,
    
    email VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,

    verification_code VARCHAR(64) NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    authentication_type VARCHAR(10) NULL CHECK (authentication_type IN ('STANDARD', 'GOOGLE', 'FACEBOOK')),
    reset_password_token VARCHAR(30) NULL,

    FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE);
~~~~

- Order Table scheme
~~~~sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    
    customer_id INT NOT NULL,
    country VARCHAR(45) NOT NULL,
    
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    shipping_cost DECIMAL(10,2) NOT NULL,
    product_cost DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    
    deliver_days INT NULL,
    deliver_date DATE NULL,

    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('CREDIT_CARD', 'PAYPAL', 'CASH_ON_DELIVERY')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('NEW', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED')),

    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE);
~~~~

### 🔥 Settings Managment
**General Setting for web page** - Config web page name, logo, currency and etc.
> ![Image](https://github.com/user-attachments/assets/50715885-a41b-4e13-be87-2d7adf7022f7)

**Mail Setting and Template**
> ![Image](https://github.com/user-attachments/assets/8ff87683-4890-4230-8fbe-9e2eec8c296f)
> ![Image](https://github.com/user-attachments/assets/f4895dec-2195-47a5-a302-04a3eed5a3f2)
> ![Image](https://github.com/user-attachments/assets/4551baf2-b898-41fa-87a0-f6f1e4e3bf99)

**Paypal**
>![Image](https://github.com/user-attachments/assets/bcfd2773-00b1-4d7e-994e-0b20b8529977)

## 🤝 Feedback and Contributions
Please feel free to contribute. Each contribution helps us grow and improve.
We appreciate your support and look forward to making our product even better with your help!

