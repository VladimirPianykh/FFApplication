package com.application.order;

import com.application.Customer;
import com.application.ProductType;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.VerifiedInput;
import com.futurefactory.Data;
import com.futurefactory.Data.Editable;

import java.time.LocalDate;

@VerifiedInput(verifier=Order.Verifier.class)
public class Order extends Data.Editable{
	public static class Verifier implements com.futurefactory.editor.Verifier{
		@Override
		public boolean verify(Editable editable,boolean isNew){Order e=(Order)editable;return e.requiredDate.isAfter(e.registrationDate);}
	}
	@EditorEntry(translation="Дата регистрации")public LocalDate registrationDate;
	@EditorEntry(translation="Дата окончания")public LocalDate requiredDate;
	@EditorEntry(translation="Информация о клиенте")public Customer customerInfo;
	@EditorEntry(translation="Вид лесопродукции")public ProductType productType;
	@EditorEntry(translation="Количество лесопродукции")public int quantity;
	@EditorEntry(translation="Дополнительная информация")public String additionalInfo;
	@EditorEntry(translation="Статус заказа")public OrderStatus status;
	public Order(
		LocalDate requiredDate,
		Customer customerInfo,
		ProductType productType,
		int quantity,
		String additionalInfo,
		OrderStatus status
	){
		super(productType==null?"Новый заказ":productType.name+" #"+(int)(Math.random()*100000000));
		this.registrationDate=LocalDate.now();
		this.requiredDate=requiredDate;
		this.customerInfo=customerInfo;
		this.productType=productType;
		this.quantity=quantity;
		this.additionalInfo=additionalInfo;
		this.status=status;
	}
	public Order(){
		this(LocalDate.now().plusDays(3),null,null,1,"",OrderStatus.DRAFT);
	}

	@Override
	public String toString() {
		return name;
	}
}
