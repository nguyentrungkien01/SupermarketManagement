package com.ou.repositories;

import com.ou.pojos.Product;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRepositoryForTest {
    // Lấy thông tin sản phẩm dựa vào id
    public Product getProductById(int proId) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query  = "SELECT * FROM Product " +
                    "WHERE pro_id = ? AND pro_is_active = TRUE";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,proId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Product product = new Product();
                product.setProId(resultSet.getInt("pro_id"));
                product.setProName(resultSet.getString("pro_name"));
                product.setProIsActive(resultSet.getBoolean("pro_is_active"));
                return product;
            }
            return null;
        }
    }
}
