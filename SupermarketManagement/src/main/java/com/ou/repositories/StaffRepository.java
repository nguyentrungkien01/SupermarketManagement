package com.ou.repositories;

import com.ou.pojos.Staff;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffRepository {
    //Lấy thông tin của người
    public Staff getStaffById(Integer staId) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query = "SELECT * FROM Person WHERE pers_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, staId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Staff staff = new Staff();
                staff.setPersFirstName(resultSet.getString("pers_first_name"));
                staff.setPersLastName(resultSet.getString("pers_last_name"));
                staff.setPersId(resultSet.getInt("pers_id"));
                return staff;
            }
        }
        return null;
    }
}
