package com.ou.pojos;

import java.math.BigDecimal;
import java.util.List;

public class MemberType {
    private Integer memtId;
    private Sale memtSale;
    private String memtName;
    private BigDecimal memtTotalMoney;
    private Boolean memtIsActive = Boolean.TRUE;
    private Sale sale;
    private List<Member> members;

    public Integer getMemtId() {
        return memtId;
    }

    public void setMemtId(Integer memtId) {
        this.memtId = memtId;
    }

    public Sale getMemtSale() {
        return memtSale;
    }

    public void setMemtSale(Sale memtSale) {
        this.memtSale = memtSale;
    }

    public String getMemtName() {
        return memtName;
    }

    public void setMemtName(String memtName) {
        this.memtName = memtName;
    }

    public BigDecimal getMemtTotalMoney() {
        return memtTotalMoney;
    }

    public void setMemtTotalMoney(BigDecimal memtTotalMoney) {
        this.memtTotalMoney = memtTotalMoney;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Boolean getMemtIsActive() {
        return memtIsActive;
    }

    public void setMemtIsActive(Boolean memtIsActive) {
        this.memtIsActive = memtIsActive;
    }
}
