package org.example.eproject_2.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;
import org.example.eproject_2.common.IMethodReturnBook;
import org.example.eproject_2.entity.ReturnB;

import java.sql.*;
import java.time.LocalDate;

public class ReturnBookService implements IMethodReturnBook<ReturnB> {

    public void setReturnBook(ReturnB returnBook, ResultSet rs) throws SQLException {
        returnBook.setReturnBookId(rs.getInt("return_book_id"));
        returnBook.setBookName(rs.getString("book_name"));
        returnBook.setReaderName(rs.getString("reader_name"));
        returnBook.setQuantity(rs.getInt("quantity"));
        returnBook.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        returnBook.setDueDate(rs.getDate("due_date").toLocalDate());
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            returnBook.setReturnDate(returnDate.toLocalDate());
        } else {
            returnBook.setReturnDate(null);
        }
        returnBook.setBookCondition(rs.getString("book_condistion"));
        returnBook.setBookAuthor(rs.getString("author_name"));
        returnBook.setBookPrice(rs.getDouble("price"));
        returnBook.setBookCategory(rs.getString("category_name"));
        returnBook.setEmail(rs.getString("email"));
        returnBook.setPhoneNumber(rs.getString("phone_number"));
        returnBook.setDob(rs.getDate("date_of_birth").toLocalDate());
    }

    public void setParamsReturns(PreparedStatement stmt, ReturnB returnB) throws SQLException {
        stmt.setString(1, returnB.getBookName());
        stmt.setString(2, returnB.getReaderName());
        stmt.setInt(3, returnB.getQuantity());
        stmt.setDate(4, Date.valueOf(returnB.getBorrowDate()));
        stmt.setDate(5, Date.valueOf(returnB.getDueDate()));
        LocalDate returnDate = returnB.getReturnDate(); // Giả sử bạn có phương thức này
        if (returnDate != null) {
            stmt.setDate(6, java.sql.Date.valueOf(returnDate));
        } else {
            stmt.setNull(6, java.sql.Types.DATE);
        }
    }

    @Override
    public boolean add(ReturnB returnB) {
        return false;
    }

    public ObservableList<ReturnB> getAll() {
        ObservableList<ReturnB> arrayList = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT return_book.return_book_id, books.book_name, readers.reader_name, " +
                "borrow_book.quantity, borrow_book.borrow_date, " +
                "borrow_book.due_date, return_book.return_date, return_book.book_condistion, " +
                "authors.author_name, bookCategories.category_name, books.price, " +
                "readers.email, readers.phone_number, readers.date_of_birth " +
                "FROM return_book " +
                "JOIN borrow_book ON return_book.borrow_book_id = borrow_book.borrow_book_id " +
                "JOIN books ON borrow_book.book_id = books.book_id " +
                "JOIN authors ON books.author_id = authors.author_id " +
                "JOIN bookCategories ON books.category_id = bookCategories.category_id " +
                "JOIN readers ON borrow_book.reader_id = readers.reader_id";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ReturnB returnBook = new ReturnB();
                setReturnBook(returnBook, rs);
                arrayList.add(returnBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @Override
    public boolean update(ReturnB obj, int id) {
        String updateReturnSql = "UPDATE return_book SET return_date = ?, book_condistion = ? WHERE return_book_id = ?";

        String selectForRestock = "SELECT rb.return_date AS prev_return_date, bb.book_id, bb.quantity " +
                "FROM return_book rb " +
                "JOIN borrow_book bb ON rb.borrow_book_id = bb.borrow_book_id " +
                "WHERE rb.return_book_id = ? FOR UPDATE";

        String restockSql = "UPDATE books SET quantity_available = quantity_available + ? WHERE book_id = ?";

        String insertViolateSql = "INSERT IGNORE INTO violate (return_book_id, reader_id) " +
                "SELECT rb.return_book_id, bb.reader_id FROM return_book rb " +
                "JOIN borrow_book bb ON rb.borrow_book_id = bb.borrow_book_id WHERE rb.return_book_id = ?";

        String updateViolate = "UPDATE violate v " +
                "JOIN ( SELECT r.reader_id, " +
                "           SUM(CASE WHEN rb.return_date IS NOT NULL AND rb.return_date <= bb.due_date THEN 1 ELSE 0 END) AS total_on_time_returns, " +
                "           SUM(CASE WHEN rb.return_date IS NOT NULL AND rb.return_date > bb.due_date THEN 1 ELSE 0 END) AS total_late_returns " +
                "       FROM readers r " +
                "       LEFT JOIN borrow_book bb ON r.reader_id = bb.reader_id " +
                "       LEFT JOIN return_book rb ON bb.borrow_book_id = rb.borrow_book_id " +
                "       GROUP BY r.reader_id ) AS totals ON v.reader_id = totals.reader_id " +
                "SET v.return_on_time = totals.total_on_time_returns, v.return_late = totals.total_late_returns " +
                "WHERE v.violate_id IS NOT NULL";

        String fineAmount = "INSERT INTO violate (return_book_id, reader_id, fine_amount) \n" +
                "SELECT r.return_book_id, b.reader_id, \n" +
                "  (GREATEST(DATEDIFF(COALESCE(r.return_date, CURRENT_DATE), b.due_date), 0) * 10000.0) + \n" +
                "  CASE \n" +
                "    WHEN r.book_condistion = 'Lost' THEN bk.price * 1.5 \n" +
                "    WHEN r.book_condistion = 'Severely damaged' THEN bk.price * 0.7 \n" +
                "    WHEN r.book_condistion = 'Slightly damaged' THEN bk.price * 0.3 \n" +
                "    ELSE 0 \n" +
                "  END \n" +
                "FROM return_book r \n" +
                "JOIN borrow_book b ON r.borrow_book_id = b.borrow_book_id \n" +
                "JOIN books bk ON b.book_id = bk.book_id \n" +
                "WHERE r.return_book_id = ? \n" +
                "ON DUPLICATE KEY UPDATE fine_amount = VALUES(fine_amount);";

        Connection conn = null;
        try {
            conn = JDBCConnect.getJDBCConnection();
            conn.setAutoCommit(false);

            // Lock and read previous state for conditional restock
            PreparedStatement sel = conn.prepareStatement(selectForRestock);
            sel.setInt(1, id);
            ResultSet rs = sel.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            Date prevReturnDate = rs.getDate("prev_return_date");
            int bookId = rs.getInt("book_id");
            int quantity = rs.getInt("quantity");

            PreparedStatement stmt = conn.prepareStatement(updateReturnSql);
            stmt.setDate(1, Date.valueOf(obj.getReturnDate()));
            stmt.setString(2, obj.getBookCondition());
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();

            // Restock only once, not for Lost
            boolean isLost = "Lost".equalsIgnoreCase(obj.getBookCondition());
            if (prevReturnDate == null && obj.getReturnDate() != null && !isLost) {
                PreparedStatement restock = conn.prepareStatement(restockSql);
                restock.setInt(1, quantity);
                restock.setInt(2, bookId);
                restock.executeUpdate();
            }

            PreparedStatement insertStmt = conn.prepareStatement(insertViolateSql);
            insertStmt.setInt(1, id);
            insertStmt.executeUpdate();

            PreparedStatement stmtViolate = conn.prepareStatement(updateViolate);
            int rowsViolateAffected = stmtViolate.executeUpdate();

            PreparedStatement stmtFineAmount = conn.prepareStatement(fineAmount);
            stmtFineAmount.setInt(1, id);
            stmtFineAmount.setInt(2, id);
            int rowsFineAffected = stmtFineAmount.executeUpdate();

            conn.commit();
            return rowsAffected > 0 || rowsViolateAffected > 0 || rowsFineAffected > 0;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public ReturnB findById(int id) {
        String sql = "SELECT return_book.return_book_id, books.book_name, readers.reader_name, " +
                "return_book.quantity, return_book.status, return_book.return_book_date, " +
                "return_book.due_date, return_book.return_date " +
                "FROM return_book " +
                "JOIN books ON return_book.book_id = books.book_id " +
                "JOIN readers ON return_book.reader_id = readers.reader_id " +
                "WHERE return_book.return_book_id = ?";

        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ReturnB returnBook = new ReturnB();
                setReturnBook(returnBook, rs);
                return returnBook;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ObservableList<ReturnB> findByNameContains(String name) {
        ObservableList<ReturnB> results = FXCollections.observableArrayList();
        for (ReturnB returnBooks : getAll()) {
            if (returnBooks.getReaderName().toLowerCase().contains(name.toLowerCase())) {
                results.add(returnBooks);
            }
        }
        return results;
    }

    public boolean update(ReturnB selectReturn) {
        return update(selectReturn, selectReturn.getReturnBookId());
    }

    public void addReturnBook(ReturnB returnB) {
    }
}
