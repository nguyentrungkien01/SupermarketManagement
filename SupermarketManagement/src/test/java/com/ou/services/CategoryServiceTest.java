/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.services;

import com.ou.pojos.Category;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author danhn
 */
public class CategoryServiceTest {
    private static Connection connection;
    private static CategoryService categoryService;
    private static CategoryServiceForTest categoryServiceForTest;
    public CategoryServiceTest(){

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        categoryService= new CategoryService();
        categoryServiceForTest = new CategoryServiceForTest();
    }

    @AfterAll
    public static void tearDownClass() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    // Kiểm tra lấy thông tin danh mục khi từ khóa truyền vào là null
    // Phải trả về tất cả các danh mục còn sử dụng
    @Test
    public void testSelectAllCategoryByNullKw() {
        try {
            List<Category> categories = categoryService.getCategories(null);
            int amount = categoryService.getCategoryAmount();
            Assertions.assertEquals(categories.size(), amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin danh mục khi từ khóa truyền vào là 1 chuỗi rỗng
    // Phải trả về tổng tất cả các danh mục
    @Test
    public void testSelectAllCategoryByEmptyKw() {
        try {
            List<Category> categories = categoryService.getCategories("");
            int amount = categoryService.getCategoryAmount();
            Assertions.assertEquals(categories.size(), amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin danh mục khi từ khóa truyền vào là tên 1 chi nhánh dưới database
    // Có 1 danh mục tên "Tên loại sản phẩm thứ 2" 
    @Test
    public void testSelectAllCategoryByValidKw() {
        try {
            List<Category> categories = categoryService.getCategories("Tên loại sản phẩm thứ 2");
            Assertions.assertEquals(categories.size(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin danh mục khi truyền vào là 1 tên chi nhánh không tồn tại dưới database
    // Không có danh mục nào tên "ABC XYZ"
    @Test
    public void testSelectAllCategoryByInValid() {
        try {
            List<Category> categories = categoryService.getCategories("ABC XYZ");
            Assertions.assertEquals(categories.size(), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra số lấy số lượng danh mục còn sử dụng dưới database
    // Có 5 danh mục dưới database nhưng chỉ còn 4 hoạt động
    @Test
    public void testGetCategoryAmount(){
        try {
            Category category = categoryServiceForTest.getCategoryById(1);
            categoryService.deleteCategory(category);
            int amount = categoryService.getCategoryAmount();
            Assertions.assertEquals(amount, 4);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm giá trị null khi thêm danh mục
    // Trả về false
    @Test
    public void testAddCategoryWithNull(){
        try {
            Assertions.assertFalse(categoryService.addCategory(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm thông tin danh mục khi thông tin không hợp lệ
    //  Trả về false
    @Test
    public void testAddCategoryWithInvalidInformation(){
        try {
            Category  category = new Category();
            category.setCatName("");
            Assertions.assertFalse(categoryService.addCategory(category));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm thông tin danh mục đã tồn tại
    // danh mục có mã là 2 đã tồn tại. Trả về false
    @Test
    public void testAddCategoryWithExist(){
        try {
            Category category = categoryServiceForTest.getCategoryById(2);
            Assertions.assertFalse(categoryService.addCategory(category));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm danh mục mới thành công
    // Trả về true
    @Test
    public void testAddCategoryWithValidInfomation(){
        try {
            Category category = new Category();
            category.setCatName("Tên loại sản phẩm thứ 7777");
            int preAmo = categoryService.getCategoryAmount();
            Assertions.assertTrue(categoryService.addCategory(category));
            int nextAmo = categoryService.getCategoryAmount();
            Assertions.assertNotEquals(preAmo, nextAmo);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    // Kiểm tra thêm giá trị null khi sửa danh mục
    // Trả về false
    @Test
    public void testUpdateCategoryWithNull(){
        try {
            Assertions.assertFalse(categoryService.updateCategory(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra sửa thông tin danh mục khi thông tin không hợp lệ
    //  Trả về false
    @Test
    public void testUpdateCategoryWithInvalidInformation(){
        try {
            Category category = categoryServiceForTest.getCategoryById(1);
            category.setCatName("");
            Assertions.assertFalse(categoryService.updateCategory(category));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra sửa thông tin danh đã tồn tại mà thông tin vừa sửa
    // trùng với thông tin danh khác đã tồn tại
    // Sửa thông tin danh 1 trùng với thông tin danh 2. Trả về false
    @Test
    public void testUpdateCategoryWithExist(){
        try {
            Category category = categoryServiceForTest.getCategoryById(1);
            category.setCatName("Tên loại sản phẩm thứ 2");
            Assertions.assertFalse(categoryService.updateCategory(category));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra sửa danh mục mới thành công
    // Trả về true
    @Test
    public void testUpdateCategoryWithValidInfomation(){
        try {
            Category category = categoryServiceForTest.getCategoryById(1);
            category.setCatName("Danh mục 14");
            Assertions.assertTrue(categoryService.addCategory(category));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra giá trị null khi xóa danh mục
    // Trả về false
    @Test
    public void testDeleteCategoryWithNull(){
        try {
            Assertions.assertFalse(categoryService.deleteCategory(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra xóa thông tin danh mục khi thông tin không hợp lệ
    //  Trả về false
    @Test
    public void testDeleteCategoryWithInvalidInformation(){
        try {
            Category category = categoryServiceForTest.getCategoryById(1);
            category.setCatId(null);
            Assertions.assertFalse(categoryService.deleteCategory(category));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra xóa thông tin danh mục không tồn tại
    @Test
    public void testDeleteCategoryWithExist(){
        try {
            Category category = categoryServiceForTest.getCategoryById(1);
            category.setCatId(9999);
            Assertions.assertFalse(categoryService.deleteCategory(category));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra xóa danh mục thành công
    // Trả về true
    @Test
    public void testDeleteCategoryWithValidInfomation(){
        try {
            Category category = categoryServiceForTest.getCategoryById(1);
            int preAmo = categoryService.getCategoryAmount();
            categoryService.deleteCategory(category);
            int nextAmo = categoryService.getCategoryAmount();
            Assertions.assertNotEquals(preAmo, nextAmo);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
