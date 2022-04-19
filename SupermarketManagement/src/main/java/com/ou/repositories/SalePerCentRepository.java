package com.ou.repositories;

import com.ou.pojos.SalePercent;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalePerCentRepository {
    // Lấy thông tin giảm giá dựa vào id
    public SalePercent getSalePercentById(Integer sperId) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query = "SELECT * FROM SalePercent WHERE sper_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sperId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                SalePercent salePercent = new SalePercent();
                salePercent.setSperPercent(resultSet.getInt("sper_percent"));
                return salePercent;
            }
        }
        return null;
    }
}
