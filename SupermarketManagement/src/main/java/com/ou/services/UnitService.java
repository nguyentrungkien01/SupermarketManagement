package com.ou.services;

import com.ou.pojos.Unit;
import com.ou.repositories.UnitRepository;

import java.sql.SQLException;
import java.util.List;

public class UnitService {
    private final static UnitRepository UNIT_REPOSITORY;

    static {
        UNIT_REPOSITORY = new UnitRepository();
    }

    // Lấy tất cả những đơn vị còn hoạt động
    public List<Unit> getAllActiveUnit() throws SQLException {
        return UNIT_REPOSITORY.getAllActiveUnit();
    }

    //Lấu thông tin của unit dựa vào tên
    public Unit getUnitByName(String uniName) throws SQLException {
        if (uniName==null)
            return null;
        return UNIT_REPOSITORY.getUnitByName(uniName);
    }
}
