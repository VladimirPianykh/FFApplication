package com.application.customer;

import com.futurefactory.Data;
import com.futurefactory.User;

public class CustomerEditable extends Data.Editable {
    private Customer customer;

    //Название компании
    public CustomerEditable(Customer customer) {
        super(customer.name());
        this.customer = customer;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.records.add(new ActionRecord(":EDITED", User.getActiveUser()));
    }
}
