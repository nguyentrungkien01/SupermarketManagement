package com.ou.services;

import com.ou.pojos.Manufacturer;
import com.ou.repositories.ManufacturerRepository;

import java.sql.SQLException;
import java.util.List;

public class ManufacturerService {
    private final static ManufacturerRepository MANUFACTURER_REPOSITORY;

    static {
        MANUFACTURER_REPOSITORY = new ManufacturerRepository();
    }

    // Lấy danh sách các nhà sản xuất
    public List<Manufacturer> getManufacturers(String kw) throws SQLException {
        return MANUFACTURER_REPOSITORY.getManufacturers(kw);
    }

    // Lấy số lượng nhà sản xuất
    public int getManufacturerAmount() throws SQLException {
        return MANUFACTURER_REPOSITORY.getManufacturerAmount();
    }

    // Thêm nhà sản xuất
    public boolean addManufacturer(Manufacturer manufacturer) throws SQLException {
        if (manufacturer == null ||
                manufacturer.getManName()==null|| manufacturer.getManName().trim().isEmpty())
            return false;
        if (MANUFACTURER_REPOSITORY.isExistManufacturer(manufacturer)) {
           Manufacturer manufacturerAdd = MANUFACTURER_REPOSITORY.getManufacturers(manufacturer.getManName()
                    .trim()).get(0);
           if (!manufacturerAdd.getManIsActive())
                return MANUFACTURER_REPOSITORY.addManufacturer(manufacturerAdd);
           return false;
        }
        return MANUFACTURER_REPOSITORY.addManufacturer(manufacturer);
    }

    // Chỉnh sửa thông tin nhà sản xuất
    public boolean updateManufacturer(Manufacturer manufacturer) throws SQLException {
        if (manufacturer == null ||
                manufacturer.getManId() == null ||
                manufacturer.getManName() ==  null || manufacturer.getManName().trim().isEmpty())
            return false;
        if (!MANUFACTURER_REPOSITORY.isExistManufacturer(manufacturer.getManId()) ||
        MANUFACTURER_REPOSITORY.isExistManufacturer(manufacturer))
            return false;
        return MANUFACTURER_REPOSITORY.updateManufacturer(manufacturer);
    }

    // Xóa nhà sản xuất
    public boolean deleteManufacturer(Manufacturer manufacturer) throws SQLException {
        if (manufacturer==null || manufacturer.getManId() == null)
            return false;
        if (!MANUFACTURER_REPOSITORY.isExistManufacturer(manufacturer.getManId()))
            return false;
        return MANUFACTURER_REPOSITORY.deleteManufacturer(manufacturer);
    }
}
