package org.example.eproject_2.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;
import org.example.eproject_2.common.IMethodViolation;
import org.example.eproject_2.entity.HistoryEntity;

import java.sql.*;

public class HistoryService implements IMethodViolation<HistoryEntity> {
    
    public void setViolation(HistoryEntity violation, ResultSet rs) throws SQLException {
        violation.setViolationId(rs.getInt("violate_id"));
        violation.setReaderName(rs.getString("reader_name"));
        violation.setFineAmount(rs.getDouble("fine_amount" ));
        violation.setPhoneNumber(rs.getString("phone_number"));
        violation.setEmail(rs.getString("email"));
        violation.setReturnOnTime(rs.getInt("return_on_time"));
        violation.setReturnLate(rs.getInt("return_late"));
    }
    @Override
    public boolean add(HistoryEntity obj) {
        return false;
    }

    public ObservableList<HistoryEntity> getAll() {
        ObservableList<HistoryEntity> violations = FXCollections.observableArrayList();
        String sql = "SELECT violate.violate_id, readers.reader_name, readers.phone_number, readers.email, " +
                "violate.fine_amount, violate.return_on_time, violate.return_late " +
                "FROM violate " +
                "JOIN readers ON violate.reader_id = readers.reader_id " +
                "JOIN return_book ON violate.return_book_id = return_book.return_book_id";
        try(Connection conn = JDBCConnect.getJDBCConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                HistoryEntity violation = new HistoryEntity();
                setViolation(violation, rs);
                violations.add(violation);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return violations;
    }

    @Override
    public boolean update(HistoryEntity obj, int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM violate WHERE violate_id = ?";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public HistoryEntity findById(int id) {
        return null;
    }

    public ObservableList<HistoryEntity> findByNameContains(String name) {
        ObservableList<HistoryEntity> results = FXCollections.observableArrayList();
        for (HistoryEntity violation : getAll()) {
            if (violation.getReaderName().toLowerCase().contains(name.toLowerCase())) {
                results.add(violation);
            }
        }
        return results;
    }
}
