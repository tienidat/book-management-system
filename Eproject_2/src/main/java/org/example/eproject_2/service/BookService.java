package org.example.eproject_2.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.eproject_2.common.IMethodBooks;
import org.example.eproject_2.entity.Books;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class BookService implements IMethodBooks<Books> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public void setBook(Books book, ResultSet rs) throws SQLException {
        book.setBookId(rs.getInt("book_id"));
        book.setBookName(rs.getString("book_name"));
        book.setAuthor(rs.getString("author_name"));
        book.setCategory(rs.getString("category_name"));
        book.setPublisher(rs.getString("publisher_name"));
        book.setIsbn(rs.getString("isbn"));
        book.setPrice(rs.getDouble("price"));
        Date yearPublished = rs.getDate("year_published");
        if (yearPublished != null) {
            LocalDate date = yearPublished.toLocalDate();
            book.setYearPublished(date);
        }
        book.setQuantity(rs.getInt("quantity_available"));
    }
    public String formatYearPublished(LocalDate date) {
        if (date != null){
            return date.format(DATE_FORMATTER);
        }
        return "N/A";
    }

    private void setParamsBooks(PreparedStatement stmt, Books obj) throws SQLException {
        stmt.setString(1, obj.getBookName());
        stmt.setString(2, obj.getAuthor());
        stmt.setString(3, obj.getPublisher());
        stmt.setDate(4, java.sql.Date.valueOf(obj.getYearPublished()));
        stmt.setString(5, obj.getIsbn());
        stmt.setDouble(6, obj.getPrice());
        stmt.setString(7, obj.getCategory());
        stmt.setInt(8, obj.getQuantity());
    }

    @Override
    public boolean add(Books bookModel) {
        String selectAuthor = "SELECT author_id FROM authors WHERE author_name = ?";
        String selectPublisher = "SELECT publisher_id FROM publishers WHERE publisher_name = ?";
        String selectCategory = "SELECT category_id FROM bookCategories WHERE category_name = ?";
        String sql = "INSERT INTO books (book_name, author_id, publisher_id, year_published, isbn, price, category_id, quantity_available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            PreparedStatement stmt = conn.prepareStatement(selectAuthor);
            stmt.setString(1, bookModel.getAuthor());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return false;
            int authorId = rs.getInt("author_id");

            stmt = conn.prepareStatement(selectPublisher);
            stmt.setString(1, bookModel.getPublisher());
            rs = stmt.executeQuery();
            if (!rs.next()) return false;
            int publisherId = rs.getInt("publisher_id");

            stmt = conn.prepareStatement(selectCategory);
            stmt.setString(1, bookModel.getCategory());
            rs = stmt.executeQuery();
            if (!rs.next()) return false;
            int categoryId = rs.getInt("category_id");

            // Thiết lập tham số cho câu lệnh INSERT
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, bookModel.getBookName());
            stmt.setInt(2, authorId);
            stmt.setInt(3, publisherId);
            stmt.setDate(4, Date.valueOf(bookModel.getYearPublished()));
            stmt.setString(5, bookModel.getIsbn());
            stmt.setDouble(6, bookModel.getPrice());
            stmt.setInt(7, categoryId);
            stmt.setInt(8, bookModel.getQuantity());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }



    public ObservableList<Books> getAll() {
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        String sql = "SELECT books.book_id, books.book_name, authors.author_name, " +
                "publishers.publisher_name, books.year_published, books.isbn, " +
                "bookCategories.category_name, books.quantity_available, books.price " +
                "FROM books " +
                "JOIN authors ON books.author_id = authors.author_id " +
                "JOIN publishers ON books.publisher_id = publishers.publisher_id " +
                "JOIN bookCategories ON books.category_id = bookCategories.category_id;";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Books book = new Books();
                setBook(book, rs);
                bookList.add(book);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return bookList;
    }


    @Override
    public boolean update(Books obj, int id) {
        String sql = "UPDATE books " +
                "SET books.book_name = ?, " +
                "books.author_id = (SELECT authors.author_id FROM authors WHERE authors.author_name = ?), " +
                "books.publisher_id = (SELECT publishers.publisher_id FROM publishers WHERE publishers.publisher_name = ?), " +
                "books.year_published = ?, books.isbn = ?, books.price = ?, " +
                "books.category_id = (SELECT bookCategories.category_id FROM bookCategories WHERE bookCategories.category_name = ?), " +
                "books.quantity_available = ? " +
                "WHERE books.book_id = ?";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            setParamsBooks(stmt, obj);
            stmt.setInt(9, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE book_id=?";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Books findById(int id) {
        return null;
    }

    public boolean update(Books selectBook) {
        return update(selectBook, selectBook.getBookId());
    }

    public boolean hasBorrowedBooks(int bookId) {
        String query = "SELECT COUNT(*) FROM borrow_book WHERE book_id = ?";
        try (Connection connection = JDBCConnect.getJDBCConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Nếu có bản ghi nào đang mượn sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ObservableList<Books> findByNameContains(String name) {
        ObservableList<Books> results = FXCollections.observableArrayList();
        for (Books book : getAll()) {
            if (book.getBookName().toLowerCase().contains(name.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }
}
