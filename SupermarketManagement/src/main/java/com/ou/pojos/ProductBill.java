package com.ou.pojos;

public class ProductBill {
    private Integer proAmount;
    private Product product;
    private Bill bill;

    public Integer getProAmount() {
        return proAmount;
    }

    public void setProAmount(Integer proAmount) {
        this.proAmount = proAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
