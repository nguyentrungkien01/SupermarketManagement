package com.ou.services;

import com.ou.pojos.Staff;
import com.ou.repositories.StaffRepository;

import java.sql.SQLException;

public class StaffService {
    private final static StaffRepository STAFF_REPOSITORY;
    static {
        STAFF_REPOSITORY  = new StaffRepository();
    }
    //Lấy thông tin của người
    public Staff getStaffById(Integer staId) throws SQLException {
        if (staId==null)
            return null;
        return STAFF_REPOSITORY.getStaffById(staId);
    }
}
