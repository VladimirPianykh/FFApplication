package com.application.order;

import com.application.customer.Customer;
import com.application.editor.EditorEntry;
import com.application.product.ProductType;
import com.futurefactory.Data;

import java.time.LocalDate;

public class Order extends Data.Editable {
	@EditorEntry(translation = "Дата регистрации") public LocalDate registrationDate;
	@EditorEntry(translation = "Дата окончания") public LocalDate requiredDate;
	@EditorEntry(translation = "Информация о клиенте") public Customer customerInfo;
	@EditorEntry(translation = "Вид лесопродукции") public ProductType productType;
	@EditorEntry(translation = "Количество лесопродукции") public int quantity;
	@EditorEntry(translation = "Дополнительная информация") public String additionalInfo;
	@EditorEntry(translation = "Статус заказа") public OrderStatus status;
	public Order(
		LocalDate requiredDate,
		Customer customerInfo,
		ProductType productType,
		//Чтобы можно было писать 12 тонн и т.д.
		int quantity,
		String additionalInfo,
		OrderStatus status
	){
		super(productType==null?"Новый заказ":productType.name+" #"+(int)(Math.random()*100000000));
		//Условия валидности
		this.registrationDate = LocalDate.now(); //Автоматическая установка даты регистрации
		this.requiredDate = requiredDate;
		this.customerInfo = customerInfo;
		this.productType = productType;
		this.quantity = quantity;
		this.additionalInfo = additionalInfo;
		this.status = status;
	}
	public Order(){
		this(LocalDate.now().plusDays(3),null,null,1,"",OrderStatus.DRAFT);
	}

	@Override
	public String toString() {
		return name;
	}
}
