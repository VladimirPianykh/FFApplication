package com.application.order;

import com.application.customer.Customer;
import com.application.product.type.ProductType;

import java.time.LocalDate;

public record Order(
        LocalDate registrationDate,
        LocalDate requiredDate,
        Customer customerInfo,
        ProductType productType,
        int quantity,
        String additionalInfo,
        OrderStatus status
) {
    // Конструктор с проверкой
    public Order {
        if (!requiredDate.isAfter(registrationDate)) {
            throw new IllegalArgumentException("Illegal date parameters");
        }
        if (status == null || status == OrderStatus.DEFAULT) {
            status = OrderStatus.DRAFT; // Статус по умолчанию
        }
    }
}
