package com.ou.services;

import com.ou.pojos.Member;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

public class MemberServiceTest {
    private static Connection connection;
    private static MemberService memberService;
    private static MemberServiceForTest memberServiceForTest;
    public MemberServiceTest() {

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        memberService = new MemberService();
        memberServiceForTest = new MemberServiceForTest();
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

    // kiểm tra lấy thông tin member khi truyền vào null id
    // trả ra null object
    @Test
    public void testGetMemberByIdNull(){
        try {
            Member member = memberService.getMemberById(null);
            Assertions.assertNull(member);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin member khi truyền vào id không tồn tại
    // trả ra null object
    @Test
    public void testGetMemberByIdNotExist(){
        try {
            Member member = memberService.getMemberById(9999999);
            Assertions.assertNull(member);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // kiểm tra lấy thông tin member khi truyền vào id tồn tại
    // trả ra limit sale object
    @Test
    public void testGetMemberByIdExist(){
        try {
            Member member = memberService.getMemberById(5);
            Assertions.assertNotNull(member);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
