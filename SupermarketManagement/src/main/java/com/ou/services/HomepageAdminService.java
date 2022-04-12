package com.ou.services;

import com.ou.pojos.Staff;
import com.ou.repositories.HomepageAdminRepository;
import com.ou.repositories.StaffRepository;

import java.sql.SQLException;

public class HomepageAdminService {
    private final static HomepageAdminRepository HOMEPAGE_ADMIN_REPOSITORY;
    static {
        HOMEPAGE_ADMIN_REPOSITORY  = new HomepageAdminRepository();
    }
    public Staff getStaff(Integer id) {
        if (id > 0) {
            try {
                return HOMEPAGE_ADMIN_REPOSITORY.getStaff(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
