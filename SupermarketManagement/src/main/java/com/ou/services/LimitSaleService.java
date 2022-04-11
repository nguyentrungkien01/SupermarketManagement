package com.ou.services;

import com.ou.pojos.LimitSale;
import com.ou.repositories.LimitSaleRepository;

import java.sql.SQLException;
import java.util.List;

public class LimitSaleService {
    private final static LimitSaleRepository LIMIT_SALE_REPOSITORY;
    static {
        LIMIT_SALE_REPOSITORY = new LimitSaleRepository();
    }

    // Lấy thông tin limit sale dựa vào id
    public LimitSale getLimitSaleById(Integer lsalId) throws SQLException {
        if (lsalId ==null)
            return null;
        return LIMIT_SALE_REPOSITORY.getLimitSaleById(lsalId);
    }
    // Lấy danh sách các loại giảm giá có thời hạn
    public List<LimitSale> getLimitSales() throws SQLException{
        return LIMIT_SALE_REPOSITORY.getLimitSales();
    }
}
