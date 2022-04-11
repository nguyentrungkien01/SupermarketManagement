package com.ou.repositories;

import com.ou.pojos.Bill;
import com.ou.pojos.ProductBill;
import com.ou.utils.DatabaseUtils;

import java.sql.*;

public class PaymentRepository {
    // Thanh toán 1 hóa đơn mới
    public boolean addBill(Bill bill) throws SQLException {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "INSERT INTO Bill (sta_id, mem_id, lsal_id, bill_created_date, " +
                    "bill_customer_money, bill_total_money, bill_total_sale_money) VALUES " +
                    "(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bill.getStaff().getPersId());
            if (bill.getMember() != null)
                preparedStatement.setInt(2, bill.getMember().getPersId());
            else
                preparedStatement.setNull(2, Types.INTEGER);
            preparedStatement.setDate(4, (Date) bill.getBillCreatedDate());
            preparedStatement.setBigDecimal(5, bill.getBillCustomerMoney());
            preparedStatement.setBigDecimal(6, bill.getBillTotalMoney());
            preparedStatement.setBigDecimal(7, bill.getBillTotalSaleMoney());
            if (preparedStatement.executeUpdate() ==1 ) {
                bill.getProductBills().forEach(pb-> {
                    try {
                        addProductBill(pb);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                return true;
            }
            return false;
        }
    }

    // Thêm các sản phẩm của hóa đơn
    private boolean addProductBill(ProductBill productBill) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query = "INSERT INTO Product_Bill (pru_id, bill_id, pro_amount) VALUES " +
                    "(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productBill.getProductUnit().getPruId());
            preparedStatement.setInt(2, productBill.getBill().getBillId());
            preparedStatement.setInt(3, productBill.getProAmount());
            return preparedStatement.executeUpdate() == 1;
        }
    }

}
