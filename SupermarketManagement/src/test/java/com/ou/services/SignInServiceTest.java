/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.services;

import com.ou.services.SignInServiceForTest;
import com.ou.services.SignInService;
import com.ou.utils.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import com.ou.pojos.Staff;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 *
 * @author Admin
 */
public class SignInServiceTest {
    private static Connection conn;
    private static SignInService signInService;
    private static SignInServiceForTest signInServiceForTest;

    public  SignInServiceTest(){
        
    }
      
    @BeforeAll
    public static void beforeAll() {
        try {
            conn = DatabaseUtils.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        signInService = new SignInService();
    }
    
    @AfterAll
    public static void afterAll() {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
    }
    
    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }


    //  Kiểm tra tài khoản không tồn tại
    //  Phải trả về null
    @Test
    public void testSignInNull() throws SQLException, NoSuchAlgorithmException{
        String username = "null";
        String password = "null";
        Staff s = signInService.getAccountMD5(username, password);
        Assertions.assertNull(s);
    }

    // Kiểm tra tài khoản có tồn tại
    // Mong muốn trả về Staff khác null
    @Test
    public void testSignIn() throws SQLException, NoSuchAlgorithmException {
        String username = "username2";
        String password = "12345678";
        Staff s = signInService.getAccountMD5(username, password);
        Assertions.assertNotNull(s);
    }

    // Kiểm tra danh sách Staff có username trùng không
    @Test
    public void testUnique() throws SQLException {
        List<String> listUsername = signInServiceForTest.getStaff();
        Set<String> r = new HashSet<>(listUsername);

        Assertions.assertEquals(listUsername.size(), r.size());
    }

    // Kiểm tra hàm MD5
    // Chuyền vào chuỗi "123456" mong muốn chuỗi ra "e10adc3949ba59abbe56e057f20f883e"
    @Test
    public void testMD5(){
        String text = "123456";
        String hope = "e10adc3949ba59abbe56e057f20f883e";
        Assertions.assertEquals(signInService.MD5(text), hope);
    }

    // Kiểm tra hàm MD5 khi chuyền ""
    // Mong muốn trả ra "d41d8cd98f00b204e9800998ecf8427e"
    @Test
    public void testMD5Null(){
        String text = "";
        String hope = "d41d8cd98f00b204e9800998ecf8427e";
        Assertions.assertEquals(signInService.MD5(text), hope);
    }
}
