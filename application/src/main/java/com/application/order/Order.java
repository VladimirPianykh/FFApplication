package com.application.order;

import com.futurefactory.Data;

public class Order extends Data.Editable{
	public OrderContext context; //TODO: ask for a bit less incapsulation
	public Order(OrderContext context){
		super(context.productType().name());
		this.context = context;
	}
}
