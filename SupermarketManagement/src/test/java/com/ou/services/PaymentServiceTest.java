package com.ou.services;

import com.ou.pojos.*;
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
    private static PaymentServiceForTest paymentServiceForTest;

    private static BillService billService;
    private static Connection connection;
    static {
        paymentService = new PaymentService();
        staffService= new StaffService();
        billService  = new BillService();
        paymentServiceForTest = new PaymentServiceForTest();
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
        Member member = new Member();
        member.setPersId(7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2022-04-13 13:20:30");
        bill.setBillCreatedDate(new Timestamp(date.getTime()));
        bill.setBillCustomerMoney(BigDecimal.valueOf(
                Double.parseDouble("100000".trim())));
        bill.setBillTotalMoney(BigDecimal.valueOf(
                Double.parseDouble("50000".trim())));
        bill.setBillTotalSaleMoney(BigDecimal.valueOf(
                Double.parseDouble("10000".trim())));
        bill.setMember(member);
        bill.setStaff(staff);
        List<ProductBill> productBills = new ArrayList<>();
        ProductUnit productUnit = new ProductUnit();
        productUnit.setPruId(1);
        ProductBill productBill = new ProductBill();
        productBill.setProductUnit(productUnit);
        productBill.setProAmount(10);
        productBills.add(productBill);
        bill.setProductBills(productBills);
        return bill;
    }

    // Th??m m???t ?????i t?????ng null
    // Tr??? v??? gi?? tr??? false
    @Test
    public void testAddBillWithNull(){
        try {
            Assertions.assertFalse(paymentService.addBill(null));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    // Th??m m???t ?????i t?????ng r???ng
    // Tr??? v??? false
    @Test
    public void testAddBillWithEmptyBill(){
        try {
            Assertions.assertFalse(paymentService.addBill(new Bill()));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Th??m m???t bill m???i m?? kh??ng c?? s???n ph???m
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

    // Th??m m???t bill m???i m?? ng??y kh???i t???o l?? null
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


    // Th??m m???t bill m???i m?? ti???n kh??ch tr??? l?? null
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

    // Th??m m???t bill m???i m?? t???ng ti???n l?? null
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

    // Th??m m???t bill m???i m?? t???ng ti???n gi???m gi?? l?? null
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

    // Th??m m???t bill m???i v???i th??ng tin h???p l???
    // return true
    @Test
    public void testAddBillWithValidInformation(){
        try {
            Bill bill = generateBill();
            Assertions.assertTrue(paymentService.addBill(bill));
            Bill billTest = paymentServiceForTest.getBillByCreatedDate("2022-04-13 13:20:30");
            Assertions.assertEquals(bill.getStaff().getPersId(), billTest.getStaff().getPersId());
            Assertions.assertEquals(bill.getMember().getPersId(), billTest.getMember().getPersId());
            Assertions.assertEquals( bill.getBillCreatedDate(), billTest.getBillCreatedDate());
            Assertions.assertEquals(0, bill.getBillCustomerMoney()
                    .subtract(billTest.getBillCustomerMoney()).intValue());
            Assertions.assertEquals(0, bill.getBillTotalMoney()
                    .subtract(billTest.getBillTotalMoney()).intValue());
            Assertions.assertEquals(0, bill.getBillTotalSaleMoney()
                    .subtract(billTest.getBillTotalSaleMoney()).intValue());
            for(int i =0; i<bill.getProductBills().size(); i++) {
                Assertions.assertEquals(bill.getProductBills().get(i).getProAmount(),
                        billTest.getProductBills().get(i).getProAmount());
                Assertions.assertEquals(bill.getProductBills().get(i).getProductUnit().getPruId(),
                        billTest.getProductBills().get(i).getProductUnit().getPruId());
            }
        }catch (ParseException | SQLException p){
            p.printStackTrace();
        }
    }
}
