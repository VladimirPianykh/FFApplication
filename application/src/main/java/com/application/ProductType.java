package com.application;

import com.futurefactory.Data;

public class ProductType extends Data.Editable{
	public ProductType(String name){super(name);}
	public ProductType(){super("Новый тип продукта");}
	public String toString(){return name;}
}
