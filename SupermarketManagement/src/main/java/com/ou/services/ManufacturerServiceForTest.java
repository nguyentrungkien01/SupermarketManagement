package com.ou.services;

import com.ou.pojos.Manufacturer;
import com.ou.repositories.ManufacturerRepositoryForTest;

import java.sql.SQLException;

public class ManufacturerServiceForTest {
    private final static ManufacturerRepositoryForTest MANUFACTURER_REPOSITORY_FOR_TEST;

    static {
        MANUFACTURER_REPOSITORY_FOR_TEST = new ManufacturerRepositoryForTest();
    }

    // Lấy thông tin chi nhánh dựa vào id
    public Manufacturer getManufacturerById(int manId) throws SQLException {
        if(manId <1 )
            return null;
        return MANUFACTURER_REPOSITORY_FOR_TEST.getManufacturerById(manId);
    }
}
