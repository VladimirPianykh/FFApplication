package com.application.order;

import com.application.customer.CustomerContext;
import com.application.product.ProductType;

import java.time.LocalDate;

public record OrderContext( //TODO: ask to use less records
	LocalDate registrationDate,
	LocalDate requiredDate,
	CustomerContext customerInfo,
	ProductType productType,
	int quantity,
	String additionalInfo,
	OrderStatus status
) {
	//TODO: talk about "lib" and "non-lib" code
	// Конструктор с проверкой
	public OrderContext {
		if (!requiredDate.isAfter(registrationDate)) {
			throw new IllegalArgumentException("Illegal date parameters");
		}
		if (status == null || status == OrderStatus.DEFAULT) {
			status = OrderStatus.DRAFT; // Статус по умолчанию
		}
	}
}
