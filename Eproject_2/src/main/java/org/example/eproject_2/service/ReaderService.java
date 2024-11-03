package org.example.eproject_2.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.eproject_2.common.IMethod;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;
import org.example.eproject_2.entity.Readers;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReaderService implements IMethod<Readers> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Cập nhật phương thức này để xử lý ngày sinh null
    public void setReaders(Readers reader, ResultSet rs) throws SQLException {
        reader.setReaderId(rs.getInt("reader_id"));
        reader.setReaderName(rs.getString("reader_name"));
        reader.setEmail(rs.getString("email"));
        reader.setPhoneNumber(rs.getString("phone_number"));
        reader.setGender(rs.getString("gender"));

        Date birthDate = rs.getDate("date_of_birth");
        if (birthDate != null) {
            LocalDate dateOfBirth = birthDate.toLocalDate();
            reader.setDateOfBirth(dateOfBirth);
        } else {
            reader.setDateOfBirth(null);
        }
    }

    // Phương thức trả về ngày sinh với định dạng DD/MM/YYYY khi cần hiển thị
    public String formatDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null) {
            return dateOfBirth.format(DATE_FORMATTER); // Định dạng ngày theo DD/MM/YYYY
        }
        return "N/A"; // Hoặc giá trị mặc định nếu ngày sinh là null
    }


    public void setParamsReaders(PreparedStatement stmt, Readers reader) throws SQLException {
        stmt.setString(1, reader.getReaderName());
        stmt.setString(2, reader.getEmail());
        stmt.setString(3, reader.getPhoneNumber());
        stmt.setString(4, reader.getGender());

        // Kiểm tra ngày sinh trước khi set vào PreparedStatement
        if (reader.getDateOfBirth() != null) {
            stmt.setDate(5, java.sql.Date.valueOf(reader.getDateOfBirth()));
        } else {
            stmt.setNull(5, Types.DATE);
        }
    }

    @Override
    public boolean add(Readers obj) {
        String sql = "INSERT INTO readers (reader_name, email, phone_number, gender, " +
                "date_of_birth) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParamsReaders(stmt, obj);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public ObservableList<Readers> getAll() {
        ObservableList<Readers> arrayList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM readers";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Readers reader = new Readers();
                setReaders(reader, rs);
                arrayList.add(reader);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return arrayList;
    }

    @Override
    public boolean update(Readers obj, int id) {
        String sql = "UPDATE readers SET reader_name = ?, email = ?, phone_number = ?, gender = ?, " +
                "date_of_birth = ? WHERE reader_id = ?";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParamsReaders(stmt, obj);
            stmt.setInt(6, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM readers WHERE reader_id=?";
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
    public List<Readers> findByName(String name) {
        List<Readers> readers = new ArrayList<>();
        String sql = "SELECT * FROM readers WHERE reader_name LIKE ?";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Readers reader = new Readers();
                setReaders(reader, rs);
                readers.add(reader);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return readers;
    }

    public boolean update(Readers selectedReader) {
        return update(selectedReader, selectedReader.getReaderId());
    }

    public ObservableList<Readers> findByNameContains(String name) {
        ObservableList<Readers> results = FXCollections.observableArrayList();
        for (Readers reader : getAll()) {
            if (reader.getReaderName().toLowerCase().contains(name.toLowerCase())) {
                results.add(reader);
            }
        }
        return results;
    }
}
