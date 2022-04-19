package com.ou.services;

import com.ou.pojos.Sale;
import com.ou.pojos.SalePercent;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SaleServiceTest {
    private static Connection connection;
    private static SaleService saleService;
    private static SaleServiceForTest saleServiceForTest;
    public SaleServiceTest() {

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        saleService = new SaleService();
        saleServiceForTest = new SaleServiceForTest();
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

    // kiểm tra lấy thông tin sale khi truyền vào null id
    // trả ra null object
    @Test
    public void testGetSalePercentByIdNull(){
        try {
            Sale sale = saleService.getSaleById(null);
            Assertions.assertNull(sale);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin sale khi truyền vào id không tồn tại
    // trả ra null object
    @Test
    public void testGetSaleByIdNotExist(){
        try {
            Sale sale = saleService.getSaleById(9999999);
            Assertions.assertNull(sale);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin sale khi truyền vào id tồn tại
    // trả ra limit sale object
    @Test
    public void testGetSaleByIdExist(){
        try {
            Sale sale = saleService.getSaleById(5);
            Assertions.assertNotNull(sale);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra thêm thông tin sale khi truyền vào sper_id là null
    // trả về false
    @Test
    public void testAddSaleBySperIdIsNull(){
        try {
            Sale sale = null;
            Assertions.assertFalse(saleService.addSale(sale));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // kiểm tra thêm thông tin sale khi truyền vào hợp lệ
    // trả về true
    @Test
    public void testAddSaleBySperIdIsValid(){
        try {
            Sale sale = new Sale();
            SalePercent salePercent = new SalePercent();
            salePercent.setSperId(1);
            salePercent.setSperIsActive(true);
            sale.setSalePercent(salePercent);
            Assertions.assertTrue(saleService.addSale(sale));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // kiểm tra sửa thông tin sale khi truyền vào sper_id là null
    // trả về false
    @Test
    public void testUpdateSaleBySperIdIsNull(){
        try {
            Sale sale = null;
            Assertions.assertFalse(saleService.updateSale(sale));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // kiểm tra thêm thông tin sale khi truyền vào hợp lệ
    // trả về true
    @Test
    public void testUpdateSaleBySperIdIsValid(){
        try {
            Sale sale = saleServiceForTest.getSaleById(1);
            sale.getSalePercent().setSperId(3);
            Assertions.assertTrue(saleService.addSale(sale));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // kiểm tra xóa thông tin sale khi truyền vào sale_id là null
    // trả về false
    @Test
    public void testDeleteSaleByIdIsNull(){
        try {
            Sale sale = null;
            Assertions.assertFalse(saleService.deleteSale(sale));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // kiểm tra xóa thông tin sale khi truyền vào sale_id hợp lệ
    // trả về true
    @Test
    public void testDeleteSaleByIdIsValid(){
        try {
            Sale sale = saleServiceForTest.getSaleById(3);
            Assertions.assertTrue(saleService.deleteSale(sale));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
