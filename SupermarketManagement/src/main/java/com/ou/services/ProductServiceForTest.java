package com.ou.services;

import com.ou.pojos.Product;
import com.ou.repositories.ProductRepositoryForTest;

import java.sql.SQLException;

public class ProductServiceForTest {
    private final static ProductRepositoryForTest PRODUCT_REPOSITORY_FOR_TEST;

    static {
        PRODUCT_REPOSITORY_FOR_TEST = new ProductRepositoryForTest();
    }

    // Lấy thông tin sản phẩm dựa vào id
    public Product getProductById(int proId) throws SQLException {
        return PRODUCT_REPOSITORY_FOR_TEST.getProductById(proId);
    }
}
