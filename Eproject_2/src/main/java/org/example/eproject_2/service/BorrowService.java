package org.example.eproject_2.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.eproject_2.common.IMethodBorrowBook;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;
import org.example.eproject_2.entity.Borrow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowService implements IMethodBorrowBook<Borrow> {

    public void setBorrowBook(Borrow borrowBook, ResultSet rs)throws SQLException {
        borrowBook.setBorrowId(rs.getInt("borrow_book_id"));
        borrowBook.setReaderName(rs.getString("reader_name"));
        borrowBook.setBookName(rs.getString("book_name"));
        borrowBook.setQuantity(rs.getInt("quantity"));
        borrowBook.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        borrowBook.setAuthor(rs.getString("author_name"));
        borrowBook.setCategory(rs.getString("category_name"));
        borrowBook.setDueDate(rs.getDate("due_date").toLocalDate());
    }

    public void setParamsBorrowBook(PreparedStatement stmt, Borrow borrowBook) throws SQLException{
        stmt.setString(1,borrowBook.getReaderName());
        stmt.setString(2, borrowBook.getBookName());
        stmt.setInt(3, borrowBook.getQuantity());
        stmt.setDate(4, java.sql.Date.valueOf(borrowBook.getBorrowDate()));
    }

    @Override
    public boolean add(Borrow obj) {
        String selectReaderSql = "SELECT reader_id FROM readers WHERE reader_name = ?";
        String selectBookSql = "SELECT book_id FROM books WHERE book_name = ?";
        String insertBorrowSql = "INSERT INTO borrow_book (reader_id, book_id, quantity, borrow_date, due_date) VALUES (?, ?, ?, ?, ?)";
        String insertReturnSql = "INSERT INTO return_book (borrow_book_id) VALUES (?)";
        String decreaseStockSql = "UPDATE books SET quantity_available = quantity_available - ? WHERE book_id = ? AND quantity_available >= ?";

        Connection conn = null;
        try {
            conn = JDBCConnect.getJDBCConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(selectReaderSql);
            stmt.setString(1, obj.getReaderName());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int readerId = rs.getInt(1);

            stmt = conn.prepareStatement(selectBookSql);
            stmt.setString(1, obj.getBookName());
            rs = stmt.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int bookId = rs.getInt("book_id");
            int requested = obj.getQuantity();
            if (requested <= 0) {
                conn.rollback();
                return false;
            }

            stmt = conn.prepareStatement(insertBorrowSql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, readerId);
            stmt.setInt(2, bookId);
            stmt.setInt(3, requested);
            stmt.setDate(4, java.sql.Date.valueOf(obj.getBorrowDate()));
            stmt.setDate(5, java.sql.Date.valueOf(obj.getDueDate()));
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int borrowBookId = rs.getInt(1);

            stmt = conn.prepareStatement(insertReturnSql);
            stmt.setInt(1, borrowBookId);
            stmt.executeUpdate();

            // Decrease stock atomically if enough available
            stmt = conn.prepareStatement(decreaseStockSql);
            stmt.setInt(1, requested);
            stmt.setInt(2, bookId);
            stmt.setInt(3, requested);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
        return false;
    }



    public ObservableList<Borrow> getAll() {
        ObservableList<Borrow> arrayList = FXCollections.observableArrayList();
        String sql = "SELECT borrow_book.borrow_book_id, readers.reader_name, books.book_name, authors.author_name, " +
                "books.price, borrow_book.quantity, borrow_book.borrow_date, bookCategories.category_name, " +
                "borrow_book.due_date " +
                "FROM borrow_book " +
                "JOIN books On borrow_book.book_id = books.book_id " +
                "JOIN readers ON borrow_book.reader_id = readers.reader_id " +
                "JOIN authors ON books.author_id = authors.author_id " +
                "JOIN bookCategories ON books.category_id = bookCategories.category_id;";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Borrow borrowBook = new Borrow();
                setBorrowBook(borrowBook, rs);
                arrayList.add(borrowBook);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return arrayList;
    }

    @Override
    public boolean update(Borrow obj, int id) {
        String selectReaderIdSql = "SELECT reader_id FROM readers WHERE reader_name = ?";
        String selectBookIdSql = "SELECT book_id FROM books WHERE book_name = ?";
        String updateBorrowBookSql = "UPDATE borrow_book SET reader_id = ?, book_id = ?, " +
                "quantity = ?, borrow_date = ?, due_date = ? WHERE borrow_book_id = ?";

        try (Connection conn = JDBCConnect.getJDBCConnection()) {
            PreparedStatement stmt = conn.prepareStatement(selectReaderIdSql);
            stmt.setString(1, obj.getReaderName());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return false;
            int readerId = rs.getInt("reader_id");

            stmt = conn.prepareStatement(selectBookIdSql);
            stmt.setString(1, obj.getBookName());
            rs = stmt.executeQuery();
            if (!rs.next()) return false;
            int bookId = rs.getInt("book_id");

            stmt = conn.prepareStatement(updateBorrowBookSql);
            stmt.setInt(1, readerId);
            stmt.setInt(2, bookId);
            stmt.setInt(3, obj.getQuantity());
            stmt.setDate(4, java.sql.Date.valueOf(obj.getBorrowDate()));
            stmt.setDate(5, java.sql.Date.valueOf(obj.getDueDate()));
            stmt.setInt(6, id);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM borrow_book WHERE borrow_book_id = ?";
        try (Connection conn =  JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Borrow> findByName(String name) {
        List<Borrow> borrows = new ArrayList<>();
        String sql = "SELECT bb.borrow_book_id, r.reader_name, b.book_name, a.author_name, " +
                "bb.quantity, bb.borrow_date, c.category_name, bb.due_date " +
                "FROM borrow_book bb " +
                "JOIN books b ON bb.book_id = b.book_id " +
                "JOIN readers r ON bb.reader_id = r.reader_id " +
                "JOIN authors a ON b.author_id = a.author_id " +
                "JOIN bookCategories c ON b.category_id = c.category_id " +
                "WHERE r.reader_name LIKE ?";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Borrow borrowBook = new Borrow();
                setBorrowBook(borrowBook, rs);
                borrows.add(borrowBook);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return borrows;
    }

    public ObservableList<Borrow> findByNameContains(String name) {
        ObservableList<Borrow> results = FXCollections.observableArrayList();
        for (Borrow borrow : getAll()) {
            if (borrow.getReaderName().toLowerCase().contains(name.toLowerCase())) {
                results.add(borrow);
            }
        }
        return results;
    }

    public boolean update(Borrow selectBorrow) {
        return update(selectBorrow, selectBorrow.getBorrowId());
    }
}
