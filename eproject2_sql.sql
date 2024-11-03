 CREATE TABLE admin (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone int,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE authors (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    author_name VARCHAR(255) UNIQUE NOT NULL
);
INSERT INTO authors (author_name) VALUES 
('J.K. Rowling'),
('George Orwell'),
('Jane Austen'),
('Mark Twain'),
('J.R.R. Tolkien');



CREATE TABLE publishers (
    publisher_id INT PRIMARY KEY AUTO_INCREMENT,
    publisher_name VARCHAR(255) UNIQUE NOT NULL
);
INSERT INTO publishers (publisher_name) VALUES 
('Penguin Random House'),
('HarperCollins'),
('Simon & Schuster'),
('Hachette Book Group'),
('Macmillan Publishers');

CREATE TABLE bookCategories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) UNIQUE NOT NULL
);
INSERT INTO bookCategories (category_name) VALUES 
('Fiction'),
('Non-fiction'),
('Science Fiction'),
('Fantasy'),
('Biography');

CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    book_name VARCHAR(255) NOT NULL,
    author_id INT,
    publisher_id INT,
    year_published DATE,
    isbn VARCHAR(20) UNIQUE,
    category_id INT,
    price decimal(10, 2),
    quantity_available INT DEFAULT 0,
    FOREIGN KEY (author_id) REFERENCES Authors(author_id),
    FOREIGN KEY (publisher_id) REFERENCES Publishers(publisher_id),
    FOREIGN KEY (category_id) REFERENCES BookCategories(category_id)
);
INSERT INTO books (book_name, author_id, publisher_id, year_published, isbn, category_id, price, quantity_available) VALUES
('Harry Potter and the Sorcerer\'s Stone', 1, 1, '1997-09-01', '9780439708180', 4, 190000.0, 10),
('1984', 2, 2, '1949-06-08', '9780451524935', 1, 140000.00, 15),
('Pride and Prejudice', 3, 3, '1813-01-28', '9781503290563', 1, 90000.00, 5),
('The Adventures of Tom Sawyer', 4, 4, '1876-04-01', '9780141321103', 1, 70000.00, 7),
('The Lord of the Rings', 5, 5, '1954-07-29', '9780544003415', 4, 240000.00, 8),
('To Kill a Mockingbird', 2, 1, '1960-07-11', '9780061120084', 1, 120000.00, 12),
('Animal Farm', 2, 2, '1945-08-17', '9780451526342', 1, 100000.00, 20),
('The Hobbit', 5, 5, '1937-09-21', '9780345339683', 4, 150000.00, 9),
('Emma', 3, 3, '1815-12-23', '9780141439587', 1, 80000.00, 6),
('The Catcher in the Rye', 2, 4, '1951-07-16', '9780316769488', 1, 100000.00, 11);


CREATE TABLE readers (
    reader_id INT PRIMARY KEY AUTO_INCREMENT,
    reader_name VARCHAR(255),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(15),
    gender VARCHAR(10),
    date_of_birth DATE
);
INSERT INTO readers (reader_name, email, phone_number, gender, date_of_birth) VALUES
('Alice Johnson', 'alice.johnson@example.com', '1234567890', 'Female', '1990-05-20'),
('Bob Smith', 'bob.smith@example.com', '0987654321', 'Male',  '1985-07-12'),
('Carol Williams', 'carol.williams@example.com', '1122334455', 'Female',  '1995-10-08'),
('David Brown', 'david.brown@example.com', '5566778899', 'Male',  '1992-11-22'),
('Eve Davis', 'eve.davis@example.com', '6677889900', 'Female',  '1988-03-30'),
('Frank Miller', 'frank.miller@example.com', '9988776655', 'Male',  '1991-08-11'),
('Grace Harris', 'grace.harris@example.com', '2233445566', 'Female',  '1993-09-17'),
('Henry Clark', 'henry.clark@example.com', '3344556677', 'Male',  '1990-12-02'),
('Irene Lewis', 'irene.lewis@example.com', '7788990011', 'Female', '1994-06-14'),
('Jack Wilson', 'jack.wilson@example.com', '5566770011', 'Male', '1989-04-28');




CREATE TABLE borrow_book (
    borrow_book_id INT PRIMARY KEY AUTO_INCREMENT,
    reader_id INT,
    book_id INT,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    quantity INT,
    status VARCHAR(50),
    FOREIGN KEY (reader_id) REFERENCES readers(reader_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);


CREATE TABLE return_book (
    return_book_id INT PRIMARY KEY AUTO_INCREMENT,
    borrow_book_id INT,
    return_date DATE NULL,
    book_condistion VARCHAR(30) NULL,
    FOREIGN KEY (borrow_book_id) REFERENCES borrow_book(borrow_book_id)
);

CREATE TABLE violate (
    violate_id INT PRIMARY KEY AUTO_INCREMENT,
    reader_id INT unique,
    return_book_id INT,
    return_on_time INT null,
    return_late INT null,
    fine_amount DECIMAL(10, 2) null,
    FOREIGN KEY (reader_id) REFERENCES readers(reader_id),
    FOREIGN KEY (return_book_id) REFERENCES return_book(return_book_id)
);

