package org.example.eproject_2.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.eproject_2.common.IMethodView;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;
import org.example.eproject_2.entity.HistoryView;

import java.sql.*;
import java.util.List;

public class HistoryViewService implements IMethodView<HistoryView> {
    public void setViolationView(HistoryView violationView, ResultSet rs) throws SQLException {
        violationView.setBookName(rs.getString("book_name"));
        violationView.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        violationView.setDueDate(rs.getDate("due_date").toLocalDate());
        violationView.setReturnDate(rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null);
        violationView.setReturnStatus(rs.getString("book_condistion"));
    }


    public ObservableList<HistoryView> getAllView(String readerName) {
        ObservableList<HistoryView> allViews = FXCollections.observableArrayList();
        String sql = "SELECT books.book_name, borrow_book.borrow_date, borrow_book.due_date, return_book.return_date, " +
                "return_book.book_condistion " +
                "FROM return_book " +
                "JOIN borrow_book ON return_book.borrow_book_id = borrow_book.borrow_book_id " +
                "JOIN books ON borrow_book.book_id = books.book_id " +
                "JOIN readers ON borrow_book.reader_id = readers.reader_id " +
                "WHERE readers.reader_name = ?";

        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, readerName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HistoryView violationView = new HistoryView();
                setViolationView(violationView, rs);
                allViews.add(violationView);
            }
            System.out.println("Total Violations Found: " + allViews.size());

        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi để có thêm thông tin
        }
        return allViews;
    }


    @Override
    public List<HistoryView> getAll() {
        return List.of();
    }
}
