package com.application.order;

import com.application.customer.Customer;
import com.application.product.ProductType;

import java.time.LocalDate;

public record OrderContext(
	LocalDate registrationDate,
	LocalDate requiredDate,
	Customer customerInfo,
	ProductType productType,
	int quantity,
	String additionalInfo,
	OrderStatus status
) {
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
