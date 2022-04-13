package com.ou.services;

import com.ou.pojos.Bill;
import com.ou.pojos.ProductBill;
import com.ou.pojos.ProductUnit;
import com.ou.pojos.Staff;
import com.ou.utils.DatabaseUtils;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentServiceTest {
    private static PaymentService paymentService;
    private static StaffService staffService;
    private static Connection connection;
    static {
        paymentService = new PaymentService();
        staffService= new StaffService();
    }
    public PaymentServiceTest(){
    }

    @BeforeAll
    public static void setUpClass() {
        try {
            connection = DatabaseUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        paymentService = new PaymentService();
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

    private Bill generateBill() throws SQLException, ParseException {
        Bill bill = new Bill();
        Staff staff = staffService.getStaffById(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2022-04-13 13:20:30");
        bill.setBillCreatedDate(new Timestamp(date.getTime()));
        bill.setBillCustomerMoney(BigDecimal.valueOf(
                Double.parseDouble("100000".trim())));
        bill.setBillTotalMoney(BigDecimal.valueOf(
                Double.parseDouble("50000".trim())));
        bill.setBillTotalSaleMoney(BigDecimal.valueOf(
                Double.parseDouble("10000".trim())));
        bill.setStaff(staff);
        List<ProductBill> productBills = new ArrayList<>();
        ProductUnit productUnit = new ProductUnit();
        productUnit.setPruId(1);
        ProductBill productBill = new ProductBill();
        productBill.setProductUnit(productUnit);
        productBill.setProAmount(10);
        productBill.setProductUnit(productUnit);
        productBills.add(productBill);
        bill.setProductBills(productBills);
        return bill;
    }

    // Thêm một đối tượng null
    // Trả về giá trị false
    @Test
    public void testAddBillWithNull(){
        try {
            Assertions.assertFalse(paymentService.addBill(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    // Thêm một đối tượng rống
    // Trả về false
    @Test
    public void testAddBillWithEmptyBill(){
        try {
            Assertions.assertFalse(paymentService.addBill(new Bill()));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Thêm một bill mới mà không có sản phẩm
    // return false
    @Test
    public void testAddBillWithEmptyProductList(){
        try {
            Bill bill = generateBill();
            bill.getProductBills().clear();
            Assertions.assertFalse(paymentService.addBill(bill));
        }catch (ParseException | SQLException p){
            p.printStackTrace();
        }
    }

    // Thêm một bill mới mà ngày khởi tạo là null
    // return false
    @Test
    public void testAddBillWithNullCreatedDate(){
        try {
            Bill bill = generateBill();
            bill.setBillCreatedDate(null);
            Assertions.assertFalse(paymentService.addBill(bill));
        }catch (ParseException | SQLException p){
            p.printStackTrace();
        }
    }


    // Thêm một bill mới mà tiền khách trả là null
    // return false
    @Test
    public void testAddBillWithNullCustomerMoney(){
        try {
            Bill bill = generateBill();
            bill.setBillCustomerMoney(null);
            Assertions.assertFalse(paymentService.addBill(bill));
        }catch (ParseException | SQLException p){
            p.printStackTrace();
        }
    }

    // Thêm một bill mới mà tổng tiền là null
    // return false
    @Test
    public void testAddBillWithNullTotalMoney(){
        try {
            Bill bill = generateBill();
            bill.setBillTotalMoney(null);
            Assertions.assertFalse(paymentService.addBill(bill));
        }catch (ParseException | SQLException p){
            p.printStackTrace();
        }
    }

    // Thêm một bill mới mà tổng tiền giảm giá là null
    // return false
    @Test
    public void testAddBillWithNullTotalSaleMoney(){
        try {
            Bill bill = generateBill();
            bill.setBillTotalSaleMoney(null);
            Assertions.assertFalse(paymentService.addBill(bill));
        }catch (ParseException | SQLException p){
            p.printStackTrace();
        }
    }

    // Thêm một bill mới với thông tin hợp lệ
    // return true
    @Test
    public void testAddBillWithValidInformation(){
        try {
            Bill bill = generateBill();
            Assertions.assertTrue(paymentService.addBill(bill));
        }catch (ParseException | SQLException p){
            p.printStackTrace();
        }
    }
}
