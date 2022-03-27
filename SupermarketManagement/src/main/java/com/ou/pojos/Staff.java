package com.ou.pojos;

import java.util.List;

public class Staff extends Person{
    private String staUsername;
    private String staPassword;
    private Boolean staIsAdmin;
    private Branch branch;
    private List<Bill> bills;

    public String getStaUsername() {
        return staUsername;
    }

    public void setStaUsername(String staUsername) {
        this.staUsername = staUsername;
    }

    public String getStaPassword() {
        return staPassword;
    }

    public void setStaPassword(String staPassword) {
        this.staPassword = staPassword;
    }

    public Boolean getStaIsAdmin() {
        return staIsAdmin;
    }

    public void setStaIsAdmin(Boolean staIsAdmin) {
        this.staIsAdmin = staIsAdmin;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
