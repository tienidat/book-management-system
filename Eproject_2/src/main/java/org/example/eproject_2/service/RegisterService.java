package org.example.eproject_2.service;


import org.example.eproject_2.common.IMethodRegister;
import org.example.eproject_2.jdbcConnect.dao.JDBCConnect;
import org.example.eproject_2.entity.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterService implements IMethodRegister {

    public void setParamsRegister(PreparedStatement stmt, Admin admin) throws SQLException {
        stmt.setString(1, admin.getUsername());
        stmt.setString(2, admin.getEmail());
        stmt.setInt(3, admin.getPhone());
        stmt.setString(4, admin.getPassword());
    }

    @Override
    public boolean add(Object obj) {
        String sql = "INSERT INTO admin (username, email, phone, password) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCConnect.getJDBCConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);){
            setParamsRegister(stmt, (Admin) obj);
            return stmt.executeUpdate() > 0;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
