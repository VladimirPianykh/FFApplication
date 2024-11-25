package com.application.workshop;

import com.futurefactory.Data.Editable;

public class Workshop extends Editable{
	public final WorkArea[]parts;
	public Workshop(String name,WorkArea[]workShopParts){
		super(name);
		this.parts=workShopParts;
	}

	@Override
	public String toString() {
		return name;
	}
}
