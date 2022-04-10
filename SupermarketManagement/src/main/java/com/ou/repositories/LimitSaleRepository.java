package com.ou.repositories;

import com.ou.pojos.LimitSale;
import com.ou.services.SaleService;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LimitSaleRepository {
    private final static SaleService SALE_SERVICE;
    static {
        SALE_SERVICE = new SaleService();
    }

    // Lấy thông tin limit sale dựa vào id
    public LimitSale getLimitSaleById(Integer lsalId) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query = "SELECT lsal.lsal_id " +
                    "FROM LimitSale lsal JOIN Sale s ON lsal.lsal_id= s.sale_id " +
                    "JOIN SalePercent sper ON s.sale_id = sper.sper_id WHERE lsal.lsal_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, lsalId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                LimitSale limitSale = new LimitSale();
                limitSale.setSale(SALE_SERVICE.getSaleById(resultSet.getInt("lsal_id")));
                return limitSale;
            }
        }
        return null;
    }
}
