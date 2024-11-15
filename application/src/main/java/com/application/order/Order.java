package com.application.order;

import com.futurefactory.Data;

public class Order extends Data.Editable{
	public OrderContext context; //TODO: make a builder
	public Order(OrderContext context){
		super(context.productType().name);
		this.context = context;
	}
}
