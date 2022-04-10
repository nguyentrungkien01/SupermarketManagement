package com.ou.services;

import com.ou.pojos.SalePercent;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SalePercentServiceTest {
    private static Connection connection;
    private static SalePercentService salePercentService;
    private static SalePercentServiceForTest salePercentServiceForTest;
    public SalePercentServiceTest() {

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        salePercentService = new SalePercentService();
        salePercentServiceForTest = new SalePercentServiceForTest();
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

    // kiểm tra lấy thông tin sale percent khi truyền vào null id
    // trả ra null object
    @Test
    public void testGetSalePercentByIdNull(){
        try {
            SalePercent salePercent = salePercentService.getSalePercentById(null);
            Assertions.assertNull(salePercent);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin sale percent khi truyền vào id không tồn tại
    // trả ra null object
    @Test
    public void testGetSalePercentByIdNotExist(){
        try {
            SalePercent salePercent = salePercentService.getSalePercentById(9999999);
            Assertions.assertNull(salePercent);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin sale percent khi truyền vào id tồn tại
    // trả ra limit sale object
    @Test
    public void testGetSalePercentByIdExist(){
        try {
            SalePercent salePercent = salePercentService.getSalePercentById(5);
            Assertions.assertNotNull(salePercent);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
