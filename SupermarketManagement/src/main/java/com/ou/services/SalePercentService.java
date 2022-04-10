package com.ou.services;

import com.ou.pojos.SalePercent;
import com.ou.repositories.SalePerCentRepository;

import java.sql.SQLException;

public class SalePercentService {
    private final static SalePerCentRepository SALE_PER_CENT_REPOSITORY;
    static {
        SALE_PER_CENT_REPOSITORY = new SalePerCentRepository();
    }
    // Lấy thông tin giảm giá dựa vào id
    public SalePercent getSalePercentById(Integer sperId) throws SQLException {
        if (sperId ==null)
            return null;
        return SALE_PER_CENT_REPOSITORY.getSalePercentById(sperId);
    }
}
