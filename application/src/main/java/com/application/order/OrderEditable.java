package com.application.order;

import com.futurefactory.Data;
import com.futurefactory.User;

public class OrderEditable extends Data.Editable {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.records.add(new ActionRecord(":EDITED", User.getActiveUser()));
    }

    public OrderEditable(Order order) {
        //Каша конечно, но что поделаешь
        super(order.productType().productType());
        this.order = order;
    }
}
