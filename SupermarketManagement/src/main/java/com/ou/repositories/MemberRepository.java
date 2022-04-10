package com.ou.repositories;

import com.ou.pojos.Member;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRepository {
    //Lấy thông tin của người
    public Member getMemberById(Integer memId) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query = "SELECT * FROM Person WHERE pers_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, memId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Member member = new Member();
                member.setPersFirstName(resultSet.getString("pers_first_name"));
                member.setPersLastName(resultSet.getString("pers_last_name"));
                member.setPersId(resultSet.getInt("pers_id"));
                return member;
            }
        }
        return null;
    }
}
