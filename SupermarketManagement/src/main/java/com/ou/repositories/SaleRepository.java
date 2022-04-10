package com.ou.repositories;

import com.ou.pojos.Sale;
import com.ou.pojos.SalePercent;
import com.ou.services.SalePercentService;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleRepository {
    private final static SalePercentService SALE_PERCENT_SERVICE;
    static {
        SALE_PERCENT_SERVICE = new SalePercentService();
    }
    // Lấy thông tin Sale dựa vào id
    public Sale getSaleById(Integer saleId ) throws SQLException{
        try(Connection connection = DatabaseUtils.getConnection()){
            String query = "SELECT sale.sale_id, sper.sper_id " +
                    "FROM Sale sale JOIN SalePercent sper ON sale.sale_id = sper.sper_id " +
                    "WHERE sale.sale_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, saleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int sperId = resultSet.getInt("sper_id");
                SalePercent salePercent = SALE_PERCENT_SERVICE.getSalePercentById(sperId);
                Sale sale = new Sale();
                sale.setSalePercent(salePercent);
                return sale;
            }
        }
        return null;
    }
}
