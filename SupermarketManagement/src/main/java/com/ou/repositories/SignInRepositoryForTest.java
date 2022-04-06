package com.ou.repositories;

import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SignInRepositoryForTest {
    // Lấy danh sách username trong bảng staff
    public List<String>  getStaff() throws SQLException {
        List<String> username = new ArrayList<>();
        try (Connection conn = DatabaseUtils.getConnection()) {
            String query = "SELECT * FROM Staff";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                username.add(rs.getString("sta_username"));
            }
        }
        return username;
    }

}
