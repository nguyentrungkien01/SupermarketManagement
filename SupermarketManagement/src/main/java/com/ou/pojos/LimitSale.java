package com.ou.pojos;

import java.util.Date;
import java.util.List;

public class LimitSale extends Sale{
    private Date lsalFromDate;
    private Date lsalToDate;
    private Sale sale;
    private List<Bill> bills;

    public Date getLsalFromDate() {
        return lsalFromDate;
    }

    public void setLsalFromDate(Date lsalFromDate) {
        this.lsalFromDate = lsalFromDate;
    }

    public Date getLsalToDate() {
        return lsalToDate;
    }

    public void setLsalToDate(Date lsalToDate) {
        this.lsalToDate = lsalToDate;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
