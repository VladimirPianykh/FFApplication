package com.application.product.type;

import com.futurefactory.Data;
import com.futurefactory.User;

//Про тип продукта тоже нужно только имя
public class ProductEditable extends Data.Editable {
    private ProductType productType;

    public ProductEditable(ProductType productType) {
        super(productType.productType());
        this.productType = productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
        this.records.add(new ActionRecord(":EDITED", User.getActiveUser()));
    }

    public ProductType getProductType() {
        return this.productType;
    }
}
