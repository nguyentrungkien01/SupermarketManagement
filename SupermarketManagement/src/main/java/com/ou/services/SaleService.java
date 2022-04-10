package com.ou.services;

import com.ou.pojos.Sale;
import com.ou.repositories.SaleRepository;

import java.sql.SQLException;

public class SaleService {
    private final static SaleRepository SALE_REPOSITORY;
    static {
        SALE_REPOSITORY = new SaleRepository();
    }

    // Lấy thông tin Sale dựa vào id
    public Sale getSaleById(Integer saleId ) throws SQLException {
        if(saleId == null)
            return null;
        return SALE_REPOSITORY.getSaleById(saleId);
    }
}
