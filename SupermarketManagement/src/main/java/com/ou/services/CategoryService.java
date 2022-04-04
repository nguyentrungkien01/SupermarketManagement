/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.services;

import com.ou.pojos.Category;
import com.ou.repositories.CategoryRepository;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author danhn
 */
public class CategoryService {
    private final static CategoryRepository CATEGORY_REPOSITORY;
    static {
        CATEGORY_REPOSITORY = new CategoryRepository();
    }
    //Lấy danh sách danh mục
    public List<Category> getCategories(String kw) throws SQLException{
        return CATEGORY_REPOSITORY.getCategories(kw);
    }
    //lấy tổng số danh mục sản phẩm
    public int getCategoryAmount() throws SQLException{
        return CATEGORY_REPOSITORY.getCategoryAmount();
    }
    //Thêm danh mục
    public boolean addCategory(Category category) throws SQLException{
        if (category == null ||
                category.getCatName() ==  null || 
                category.getCatName().trim().isEmpty())
            return false;
        if (CATEGORY_REPOSITORY.isExistCategory(category)) {
            Category categoryAdd = CATEGORY_REPOSITORY.getCategories(category.getCatName()
                    .trim()).get(0);
            if (!categoryAdd.getCatIsActive())
                return CATEGORY_REPOSITORY.addCategory(categoryAdd);
            return false;
        }
        return CATEGORY_REPOSITORY.addCategory(category);
    }
    //Sửa danh mục
    public boolean updateCategory(Category category) throws SQLException{
        if (category == null ||
                category.getCatId() == null ||
                category.getCatName() ==  null || 
                category.getCatName().trim().isEmpty())
            return false;
        if (CATEGORY_REPOSITORY.isExistCategory(category) || !CATEGORY_REPOSITORY.isExistCategory(category.getCatId()))
            return false;
        return CATEGORY_REPOSITORY.updateCategory(category);
    }
    //Xóa danh mục
    public boolean deleteCategory(Category category) throws SQLException{
        if (category == null ||category.getCatId() == null)
            return false;
        if (!CATEGORY_REPOSITORY.isExistCategory(category.getCatId()))
            return false;
        return CATEGORY_REPOSITORY.deleteCategory(category);
    }
}
