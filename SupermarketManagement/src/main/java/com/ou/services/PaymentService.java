package com.ou.services;

import com.ou.pojos.Bill;
import com.ou.repositories.PaymentRepository;

import java.sql.SQLException;

public class PaymentService {
    private final static PaymentRepository PAYMENT_REPOSITORY;
    static {
        PAYMENT_REPOSITORY  = new PaymentRepository();
    }
    // Thanh toán 1 hóa đơn mới
    public boolean addBill(Bill bill) throws SQLException {
        if(bill == null ||
                bill.getProductBills() == null || bill.getProductBills().size() ==0)
            return false;
        return PAYMENT_REPOSITORY.addBill(bill);
    }
}
