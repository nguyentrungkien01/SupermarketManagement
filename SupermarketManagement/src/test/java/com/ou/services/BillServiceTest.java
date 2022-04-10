package com.ou.services;

import com.ou.pojos.Bill;
import com.ou.pojos.ProductBill;
import com.ou.utils.DatabaseUtils;
import com.ou.utils.PersonType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillServiceTest {
    private static Connection connection;
    private static BillService billService;
    private static BillServiceForTest billServiceForTest;
    public BillServiceTest() {

    }
    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        billService = new BillService();
        billServiceForTest = new BillServiceForTest();
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

    // Kiểm tra lấy tất cả thông tin của các hóa đơn khi truyền vào tham số là null
    // Có 6 bill dưới db. Kết quả trả ra phải là 6
    @Test
    public void testGetBillsByNullParams(){
        try {
            List<Bill> bills = billService.getBills(null, null , null);
            Assertions.assertEquals(6, bills.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy tất cả thông tin của các hóa đơn khi truyền vào tham số là name là 1 chuỗi rỗng
    // Có 6 bill dưới db. Kết quả trả ra phải là 6
    @Test
    public void testGetBillsByEmptyName(){
        try {
            List<Bill> bills = billService.getBills("", null , null);
            Assertions.assertEquals(6, bills.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy tất cả thông tin của các hóa đơn khi truyền vào tham số lọc theo ngày là 1 list rống
    // Có 6 bill dưới db. Kết quả trả ra phải là 6
    @Test
    public void testGetBillsByEmptyDatesParams(){
        try {
            List<Bill> bills = billService.getBills("", null , new ArrayList<>());
            Assertions.assertEquals(6, bills.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin của tất cả các hóa đơn dựa vào tên của khách hàng
    // không tồn tại dưới database.
    // Khách hàng tên "Tên khách hàng thứ 99999999" không tồn tại. Cẩn trả về 1 danh sách rống
    @Test
    public void testGetBillsByMemNameNoExist(){
        try {
            List<Bill> bills = billService.getBills("Tên khách hàng thứ 99999999", PersonType.MEMBER , null);
            Assertions.assertEquals(0, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin của tất cả các hóa đơn dựa vào tên của khách hàng
    // Có tồn tại dưới db
    // Khách hàng trong tên có chữ "V" có tồn tại và có 1 hóa đơn
    @Test
    public void testGetBillsByMemNameExist(){
        try {
            List<Bill> bills = billService.getBills("V", PersonType.MEMBER , null);
            Assertions.assertEquals(1, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin của tất cả các hóa đơn dựa vào tên của nhân viên lập hóa đơn
    // Không tồn tại dưới db
    // Nhân viên có tên "Tên nhân viên thứ 9999999" không có
    @Test
    public void testGetBillByStaNameNoExist(){
        try {
            List<Bill> bills = billService.getBills("Tên nhân viên thứ 9999999", PersonType.STAFF , null);
            Assertions.assertEquals(0, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin của tất cả các hóa đơn dựa vào tên của nhân viên lập hóa đơn
    // tồn tại dưới db
    // Nhân viên trong tên có chứ "L" đã lập 2 hóa đơn
    @Test
    public void testGetBillByStaNameExist(){
        try {
            List<Bill> bills = billService.getBills("L", PersonType.STAFF , null);
            Assertions.assertEquals(2, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    // Kiểm tra lấy thông tin tất cả các hóa đơn dựa vào tên khác hàng và ngày tạo không tồn tại
    // Mong chờ trả ra 1
    @Test
    public void testGetBillByMemNameWithNotExistSpecificDate(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("2222-02-22");
            List<Bill> bills = billService.getBills("Long", PersonType.MEMBER , dates);
            Assertions.assertEquals(0, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin tất cả các hóa đơn dựa vào tên khác hàng và ngày tạo tồn tại
    // Mong chờ trả ra 1
    @Test
    public void testGetBillByMemNameWithExistSpecificDate(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("2022-02-04");
            List<Bill> bills = billService.getBills("Long", PersonType.MEMBER , dates);
            Assertions.assertEquals(1, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thông tin lấy thông tin tất cả các hóa đơn dựa vào tên khác hàng và ngày tạo
    // không tồn tại trong khoảng thời gian cụ thể
    // Trả về 0
    @Test
    public void testGetBillByMemNameWithNotExistPeriodDates(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("1999-01-01");
            dates.add("1999-01-02");
            List<Bill> bills = billService.getBills("Long", PersonType.MEMBER , dates);
            Assertions.assertEquals(0, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thông tin lấy thông tin tất cả các hóa đơn dựa vào tên khác hàng và ngày tạo
    // tồn tại trong khoảng thời gian cụ thể
    // Trả về 1
    @Test
    public void testGetBillByMemNameWithExistPeriodDates(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("2022-02-01");
            dates.add("2022-02-04");
            List<Bill> bills = billService.getBills("Long", PersonType.MEMBER , dates);
            Assertions.assertEquals(1, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin tất cả các hóa đơn dựa vào tên nhân viên và ngày tạo không tồn tại
    // Mong chờ trả ra 0
    @Test
    public void testGetBillByStaNameWithNotExistSpecificDate(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("1999-03-04");
            List<Bill> bills = billService.getBills("Loan", PersonType.STAFF , dates);
            Assertions.assertEquals(0, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy thông tin tất cả các hóa đơn dựa vào tên nhân viên và ngày tạo tồn tại
    // Mong chờ trả ra 1
    @Test
    public void testGetBillByStaNameWithExistSpecificDate(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("2021-03-04");
            List<Bill> bills = billService.getBills("Loan", PersonType.STAFF , dates);
            Assertions.assertEquals(1, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thông tin lấy thông tin tất cả các hóa đơn dựa vào tên nhân viên và ngày tạo
    // không tồn tại trong khoảng thời gian cụ thể
    // Trả về 0
    @Test
    public void testGetBillByStaNameWithNotExistPeriodDates(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("1999-02-01");
            dates.add("1999-02-04");
            List<Bill> bills = billService.getBills("Loan", PersonType.STAFF , dates);
            Assertions.assertEquals(0, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Kiểm tra thông tin lấy thông tin tất cả các hóa đơn dựa vào tên nhân viên và ngày tạo
    // tồn tại trong khoảng thời gian cụ thể
    // Trả về 2
    @Test
    public void testGetBillByStaNameWithExistPeriodDates(){
        try {
            List<String> dates = new ArrayList<>();
            dates.add("2021-03-04");
            dates.add("2022-02-06");
            List<Bill> bills = billService.getBills("Loan", PersonType.STAFF , dates);
            Assertions.assertEquals(2, bills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Lấy số lượng hóa đơn
    // Có 6 hóa đơn
    @Test
    public void testGetBillAmount(){
        try {
            int amount = billService.getBillAmount();
            Assertions.assertEquals(6, amount);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // Lấy danh sách sản phẩm của hóa đơn có mã không tồn tại
    // Hóa đơn có mã là 9999 không tồn tại, trả ra danh sách rống
   @Test
    public void getProductBillsByExistBillId(){
        try {
            List<ProductBill> productBills = billService.getProductBillsByBillId(999);
            Assertions.assertEquals(0, productBills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Lấy danh sách sản phẩm của hóa đơn có mã không tồn tại
    // Hóa đơn có mã là 5 có 7 sản phẩm
    @Test
    public void getProductBillsByBillId(){
        try {
            List<ProductBill> productBills = billService.getProductBillsByBillId(5);
            Assertions.assertEquals(7, productBills.size());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
