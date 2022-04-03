/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.repositories;

import com.ou.pojos.Category;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author danhn
 */
public class CategoryRepositoryForTest {
    // Lấy thông tin  danh mục dựa vào id
    public Category getCategoryById(int catId) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query  = "SELECT * FROM Category " +
                    "WHERE cat_id = ? AND cat_is_active = TRUE";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,catId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Category category = new Category();
                category.setCatId(resultSet.getInt("bra_id"));
                category.setCatName(resultSet.getString("bra_name"));
                return category;
            }
            return null;
        }
    }
}
