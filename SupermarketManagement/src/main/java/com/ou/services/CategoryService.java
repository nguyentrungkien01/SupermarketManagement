package com.ou.services;

import com.ou.pojos.Category;
import com.ou.repositories.CategoryRepository;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final static CategoryRepository CATEGORY_REPOSITORY;

    static {
        CATEGORY_REPOSITORY = new CategoryRepository();
    }
    // Lấy tất cả những category còn hoạt động
    public List<Category> getAllActiveCategory() throws SQLException {
        return CATEGORY_REPOSITORY.getAllActiveCategory();
    }

    // Lấy thông tin của category dựa vào tên loại hàng
    public Category getCategoryByName(String catName) throws SQLException{
        if (catName == null)
            return null;
        return CATEGORY_REPOSITORY.getCategoryByName(catName);
    }
}
