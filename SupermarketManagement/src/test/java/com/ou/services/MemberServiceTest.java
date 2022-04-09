package com.ou.services;

import com.ou.pojos.Member;
import com.ou.repositories.MemberRepositoryForTest;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MemberServiceTest {
    private static Connection connection;
    private static MemberService memberService;
    private static MemberRepositoryForTest memberRepositoryForTest;
    public MemberServiceTest(){

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        memberService= new MemberService();
        memberRepositoryForTest = new MemberRepositoryForTest();
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

    // Kiểm tra lấy thông tin thành viên khi từ khóa truyền vào là null
    // Phải trả về tất cả các thành viên đang còn hoạt động
    @Test
    public void testSelectAllMemberByNullKw() {
        try {
            List<Member> members = memberRepositoryForTest.getMembers(null);
            int amount = memberService.getMemberAmount();
            Assertions.assertEquals(amount, members.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin thành viên khi từ khóa truyền vào là chuỗi rỗng
    // Phải trả về tất cả các thành viên đang còn hoạt động
    @Test
    public void testSelectAllMemberByEmptyKw() {
        try {
            List<Member> members = memberRepositoryForTest.getMembers("");
            int amount = memberService.getMemberAmount();
            Assertions.assertEquals(amount, members.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin thành viên tên là "Trần Anh"
    // Phải trả về 1 thành viên là Trần Anh
    @Test
    public void testSelectAllMemberByValidKw() {
        try {
            List<Member> members = memberRepositoryForTest.getMembers("Trần Anh");
            Assertions.assertEquals(1, members.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin thành viên khi từ khóa truyền vào không tồn tại dưới DATABASE
    // Không có thành viên nào tên "Nguyễn Thành Danh"
    @Test
    public void testSelectAllMemberByInValidKw() {
        try {
            List<Member> members = memberRepositoryForTest.getMembers("Nguyễn Thành Danh");
            Assertions.assertEquals(0, members.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy số lượng thành viên còn hoạt động dưới database
    // Có 4 thành viên dưới database nhưng chỉ còn 3 hoạt động
    @Test
    public void testGetMemberAmount(){
        try {
            Member member = memberRepositoryForTest.getMemberById(7);
            memberService.deleteMember(member);
            int amount = memberService.getMemberAmount();
            Assertions.assertEquals(3, amount);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm giá trị null khi thêm thành viên
    // Trả về false
    @Test
    public void testAddMemberWithNull(){
        try {
            Assertions.assertFalse(memberService.addMember(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm thông tin thành viên khi thông tin không hợp lệ
    //  Trả về false
    @Test
    public void testAddMemberWithInvalidInformation(){
        try {
            Member member = new Member();
            member.setPersFirstName("");
            member.setPersLastName("");
            member.setPersIdCard("");
            Assertions.assertFalse(memberService.addMember(member));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm thông tin thành viên đã tồn tại
    // thành viên có mã là 6 đã tồn tại. Trả về false
    @Test
    public void testAddMemberWithExist(){
        try {
            Member member = memberRepositoryForTest.getMemberById(6);
            Assertions.assertFalse(memberService.addMember(member));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm thành viên mới thành công
    // Trả về true
    @Test
    public void testAddMemberWithValidInfomation(){
        try {
            Member member = new Member();
            member.setPersLastName("Nguyen Van");
            member.setPersFirstName("A");
            member.setPersIdCard("352430926");
            member.setPersPhoneNumber("0567255612");
            member.setPersIsActive(true);
            Date dob = new Date(2000,12,12);
            member.setPersDateOfBirth(dob);
            member.setPersSex((byte) 1);
            int preAmo = memberService.getMemberAmount();
            Assertions.assertTrue(memberService.addMember(member));
            int nextAmo = memberService.getMemberAmount();
            Assertions.assertNotEquals(preAmo, nextAmo);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thêm giá trị null khi sửa thành viên
    // Trả về false
    @Test
    public void testUpdateMemberWithNull(){
        try {
            Assertions.assertFalse(memberService.updateMember(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra sửa thông tin thành viên khi thông tin không hợp lệ
    //  Trả về false
    @Test
    public void testUpdateMemberWithInvalidInformation(){
        try {
            Member member = memberRepositoryForTest.getMemberById(6);
            member.setPersPhoneNumber("");
            member.setPersIdCard("");
            Assertions.assertFalse(memberService.updateMember(member));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra sửa thông tin thành viên đã tồn tại mà thông tin vừa sửa trùng với thông tin thành viên khác đã tồn tại
    // Sửa thông tin thành viên 6 trùng với thông tin thành viên 5. Trả về false
    @Test
    public void testUpdateMemberWithExist(){
        try {
            Member member = memberRepositoryForTest.getMemberById(6);
            member.setPersIdCard("001215432158");
            Assertions.assertFalse(memberService.updateMember(member));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra sửa thành viên thành công
    // Trả về true
    @Test
    public void testUpdateMemberWithValidInfomation(){
        try {
            Member member = memberRepositoryForTest.getMemberById(7);
            member.setPersFirstName("Danh");
            Assertions.assertTrue(memberService.updateMember(member));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra giá trị null khi xóa thành viên
    // Trả về false
    @Test
    public void testDeleteMemberWithNull(){
        try {
            Assertions.assertFalse(memberService.deleteMember(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra xóa thông tin thành viên khi thông tin không hợp lệ
    //  Trả về false
    @Test
    public void testDeleteMemberWithInvalidInformation(){
        try {
            Member member = memberRepositoryForTest.getMemberById(5);
            member.setPersId(null);
            Assertions.assertFalse(memberService.deleteMember(member));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra xóa thông tin thành viên không tồn tại
    @Test
    public void testDeleteMemberWithExist(){
        try {
            Member member = memberRepositoryForTest.getMemberById(5);
            member.setPersId(9999);
            Assertions.assertFalse(memberService.deleteMember(member));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra xóa thành viên thành công
    // Trả về true
    @Test
    public void testDeleteMemberWithValidInfomation(){
        try {
            Member member = memberRepositoryForTest.getMemberById(8);
            int preAmo = memberService.getMemberAmount();
            memberService.deleteMember(member);
            int nextAmo = memberService.getMemberAmount();
            Assertions.assertNotEquals(preAmo, nextAmo);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
