package com.ou.services;

import com.ou.pojos.Branch;
import com.ou.repositories.BranchRepository;

import java.sql.SQLException;
import java.util.List;

public class BranchService {
    private final static BranchRepository BRANCH_REPOSITORY;

    static {
        BRANCH_REPOSITORY = new BranchRepository();
    }

    // Lấy danh sách các chi nhánh
    public List<Branch> getBranches(String kw) throws SQLException {
        return BRANCH_REPOSITORY.getBranches(kw);
    }

    // Lấy số lượng chi nhánh
    public int getBranchAmount() throws SQLException {
        return BRANCH_REPOSITORY.getBranchAmount();
    }

    // Thêm chi nhánh
    public boolean addBranch(Branch branch) throws SQLException {
        if (branch == null ||
                branch.getBraName()==null|| branch.getBraName().trim().isEmpty()||
                branch.getBraAddress() == null|| branch.getBraAddress().trim().isEmpty())
            return false;
        if (BRANCH_REPOSITORY.isExistBranch(branch))
            return false;
        return BRANCH_REPOSITORY.addBranch(branch);
    }

    // Chỉnh sửa thông tin chi nhánh
    public boolean updateBranch(Branch branch) throws SQLException {
        if (branch == null ||
                branch.getBraId() == null ||
                branch.getBraName() ==  null || branch.getBraName().trim().isEmpty()||
                branch.getBraAddress() == null || branch.getBraAddress().trim().isEmpty())
            return false;
        if (BRANCH_REPOSITORY.isExistBranch(branch))
            return false;
        return BRANCH_REPOSITORY.updateBranch(branch);
    }

    // Xóa chi nhánh
    public boolean deleteBranch(Branch branch) throws SQLException {
        if (branch==null || branch.getBraId() == null)
            return false;
        return BRANCH_REPOSITORY.deleteBranch(branch);
    }
}
