package com.application.product;

import com.futurefactory.Data;
import com.futurefactory.User;

//Про тип продукта тоже нужно только имя
public class Product extends Data.Editable {
    private ProductType productType;

    public Product(ProductType productType) {
        super(productType.name());
        this.productType = productType;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
        this.records.add(new ActionRecord(":EDITED", User.getActiveUser()));
    }
}
