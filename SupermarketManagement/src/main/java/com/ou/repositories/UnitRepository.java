package com.ou.repositories;

import com.ou.pojos.Unit;
import com.ou.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitRepository {

    // Lấy tất cả những đơn vị còn hoạt động
    public List<Unit> getAllActiveUnit() throws SQLException {
        List<Unit> units = new ArrayList<>();
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT uni_name FROM Unit WHERE uni_is_active = TRUE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Unit unit = new Unit();
                unit.setUniName(resultSet.getString("uni_name"));
                units.add(unit);
            }
        }
        return units;
    }

    //Lấu thông tin của unit dựa vào tên
    public Unit getUnitByName(String uniName) throws SQLException{
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT uni_id, uni_name FROM Unit WHERE uni_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, uniName.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Unit unit = new Unit();
                unit.setUniId(resultSet.getInt("uni_id"));
                unit.setUniName(resultSet.getString("uni_name"));
                return unit;
            }
        }
        return null;
    }
}
