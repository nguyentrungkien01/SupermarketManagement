package com.ou.services;

import com.ou.pojos.Staff;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class StaffServiceTest {
    private static Connection connection;
    private static StaffService staffService;
    private static StaffServiceForTest staffServiceForTest;
    public StaffServiceTest() {

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        staffService = new StaffService();
        staffServiceForTest = new StaffServiceForTest();
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

    // kiểm tra lấy thông tin staff khi truyền vào null id
    // trả ra null object
    @Test
    public void testGetStaffByIdNull(){
        try {
            Staff staff = staffService.getStaffById(null);
            Assertions.assertNull(staff);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin staff khi truyền vào id không tồn tại
    // trả ra null object
    @Test
    public void testGetStaffByIdNotExist(){
        try {
            Staff staff = staffService.getStaffById(9999999);
            Assertions.assertNull(staff);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin staff khi truyền vào id tồn tại
    // trả ra limit sale object
    @Test
    public void testGetStaffByIdExist(){
        try {
            Staff staff = staffService.getStaffById(1);
            Assertions.assertNotNull(staff);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
