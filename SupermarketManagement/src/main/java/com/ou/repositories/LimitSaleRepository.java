package com.ou.repositories;

import com.ou.pojos.LimitSale;
import com.ou.pojos.SalePercent;
import com.ou.services.SaleService;
import com.ou.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LimitSaleRepository {
    private final static SaleService SALE_SERVICE;
    static {
        SALE_SERVICE = new SaleService();
    }

    // Lấy thông tin limit sale dựa vào id
    public LimitSale getLimitSaleById(Integer lsalId) throws SQLException {
        try(Connection connection = DatabaseUtils.getConnection()){
            String query = "SELECT lsal.lsal_id, lsal.lsal_from_date, lsal.lsal_to_date " +
                    "FROM LimitSale lsal JOIN Sale s ON lsal.lsal_id= s.sale_id " +
                    "JOIN SalePercent sper ON s.sale_id = sper.sper_id WHERE lsal.lsal_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, lsalId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                LimitSale limitSale = new LimitSale();
                limitSale.setSale(SALE_SERVICE.getSaleById(resultSet.getInt("lsal_id")));
                limitSale.setLsalFromDate(resultSet.getDate("lsal_from_date"));
                limitSale.setLsalToDate(resultSet.getDate("lsal_to_date"));
                return limitSale;
            }
        }
        return null;
    }

    // Lấy danh sách các loại giảm giá có thời hạn
    public List<LimitSale> getLimitSales() throws SQLException{
        List<LimitSale> limitSales = new ArrayList<>();
        try(Connection connection =DatabaseUtils.getConnection()){
            String query = "SELECT lsal.lsal_id, lsal.lsal_from_date, lsal.lsal_to_date, sper.sper_percent " +
                    "FROM LimitSale lsal JOIN Sale s ON lsal.lsal_id = s.sale_id " +
                    "JOIN SalePercent sper ON s.sper_id = sper.sper_id " +
                    "WHERE s.sale_is_active = TRUE " +
                    "ORDER BY lsal.lsal_to_date DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                LimitSale limitSale = new LimitSale();
                SalePercent salePercent = new SalePercent();
                limitSale.setSaleId(resultSet.getInt("lsal_id"));
                limitSale.setLsalFromDate(resultSet.getDate("lsal_from_date"));
                limitSale.setLsalToDate(resultSet.getDate("lsal_to_date"));
                salePercent.setSperPercent(resultSet.getFloat("sper_percent"));
                limitSale.setSalePercent(salePercent);
                limitSales.add(limitSale);
            }
            return limitSales;
        }
    }
}
