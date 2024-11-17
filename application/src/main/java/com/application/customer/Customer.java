package com.application.customer;

import com.futurefactory.Data;

public class Customer extends Data.Editable{
	public Customer(String name){super(name);}
	public Customer(){super("Новый клиент");}

	@Override
	public String toString() {
		return name;
	}
}
