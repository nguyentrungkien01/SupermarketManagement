package com.ou.services;

import com.ou.pojos.Staff;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class HomepageAdminTest {
    private static Connection connection;
    private static HomepageAdminService homepageAdminService;
    public HomepageAdminTest() {

    }

    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        homepageAdminService = new HomepageAdminService();
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

    // lấy thông tin staff với không tòn tại
    // trả ra null
    @Test
    public void testGetStaffFailed() {
        try {
            Staff staff = homepageAdminService.getStaff(0);
            Assertions.assertNull(staff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // lấy thông tin staff với id không tồn tại
    // trả ra null
    @Test
    public void testGetStaffFailed2() {
        try {
            Staff staff = homepageAdminService.getStaff(-1);
            Assertions.assertNull(staff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // lấy thông tin staff với id không tồn tại
    // trả ra null
    @Test
    public void testGetStaffFailed3() {
        try {
            Staff staff = homepageAdminService.getStaff(9999999);
            Assertions.assertNull(staff.getStaUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // lấy thông tin staff với id tồn tại
    // trả ra not null
    @Test
    public void testGetStaffPass() {
        try {
            Staff staff = homepageAdminService.getStaff(1);
            Assertions.assertNotNull(staff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
