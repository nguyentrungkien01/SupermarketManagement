package com.ou.services;

import com.ou.pojos.LimitSale;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class LimitSaleServiceTest {
    private static Connection connection;
    private static LimitSaleService limitSaleService;
    private static LimitSaleServiceForTest limitSaleServiceForTest;
    public LimitSaleServiceTest() {

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        limitSaleService = new LimitSaleService();
        limitSaleServiceForTest = new LimitSaleServiceForTest();
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

    // kiểm tra lấy thông tin limit sale khi truyền vào null id
    // trả ra null object
    @Test
    public void testGetLimitSaleByIdNull(){
        try {
            LimitSale limitSale = limitSaleService.getLimitSaleById(null);
            Assertions.assertNull(limitSale);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin limit sale khi truyền vào id không tồn tại
    // trả ra null object
    @Test
    public void testGetLimitSaleByIdNotExist(){
        try {
            LimitSale limitSale = limitSaleService.getLimitSaleById(99999999);
            Assertions.assertNull(limitSale);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin limit sale khi truyền vào id tồn tại
    // trả ra limit sale object
    @Test
    public void testGetLimitSaleByIdExist(){
        try {
            LimitSale limitSale = limitSaleService.getLimitSaleById(5);
            Assertions.assertNotNull(limitSale);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
