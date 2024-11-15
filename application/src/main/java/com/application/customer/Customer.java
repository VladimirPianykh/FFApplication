package com.application.customer;

import com.futurefactory.Data;

public class Customer extends Data.Editable{
	//Название компании
	public Customer(String name){
		super(name);
	}
	//TODO: explain `ActionRecord`s system.
	// this.records.add(new ActionRecord(":EDITED", User.getActiveUser()));
}
