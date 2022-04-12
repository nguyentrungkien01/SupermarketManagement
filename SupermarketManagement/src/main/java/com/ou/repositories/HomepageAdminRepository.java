package com.ou.repositories;

import com.ou.pojos.Staff;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomepageAdminRepository {
    // lấy thông tin staff
    public Staff getStaff(Integer id) throws SQLException {
        Staff staff = new Staff();
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT * FROM Person,Staff " +
                    "WHERE Person.pers_id = Staff.sta_id AND Staff.sta_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                staff.setPersId(rs.getInt("pers_id"));
                staff.setPersLastName(rs.getString("pers_last_name"));
                staff.setPersFirstName(rs.getString("pers_first_name"));
                staff.setPersIdCard(rs.getString("pers_id_card"));
                staff.setPersPhoneNumber(rs.getString("pers_phone_number"));
                staff.setSex(rs.getByte("pers_sex")==1 ? "Nam" : "Nữ");
                staff.setPersDateOfBirth(rs.getDate("pers_date_of_birth"));
                staff.setPersJoinedDate(rs.getDate("pers_joined_date"));
                staff.setBranchName(getBranchNameById(rs.getInt("bra_id")));
                staff.setStaUsername(rs.getString("sta_username"));
            }
        }
        return staff;
    }
    //Lấy tên nhánh từ braId
    public String getBranchNameById(int id) throws SQLException {
        String branchName = "";
        if(id > 0) {
            try (Connection connection = DatabaseUtils.getConnection()) {
                String query = "SELECT bra_name FROM Branch WHERE bra_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, id);

                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()){
                    branchName = rs.getString("bra_name");
                    return branchName;
                }
            }
        }
        return branchName;
    }

}
