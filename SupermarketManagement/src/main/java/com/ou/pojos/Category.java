package com.ou.pojos;

import java.util.List;

public class Category {
    private Integer catId;
    private Integer catName;
    private List<Product> products;

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getCatName() {
        return catName;
    }

    public void setCatName(Integer catName) {
        this.catName = catName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
