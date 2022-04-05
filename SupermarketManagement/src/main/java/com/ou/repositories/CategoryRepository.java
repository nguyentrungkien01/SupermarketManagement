package com.ou.repositories;

import com.ou.pojos.Category;
import com.ou.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    // Lấy tất cả những category còn hoạt động
    public List<Category> getAllActiveCategory() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT cat_name FROM Category WHERE cat_is_active = TRUE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Category category = new Category();
                category.setCatName(resultSet.getString("cat_name"));
                categories.add(category);
            }
        }
        return categories;
    }

    // Lấy thông tin của category dựa vào tên loại hàng
    public Category getCategoryByName(String catName) throws SQLException{
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT cat_id, cat_name FROM Category WHERE cat_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, catName.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Category category = new Category();
                category.setCatId(resultSet.getInt("cat_id"));
                category.setCatName(resultSet.getString("cat_name"));
                return category;
            }
        }
        return null;
    }
}
