package com.application.order;

import com.application.customer.Customer;
import com.application.product.ProductType;
import com.futurefactory.Data;

import java.time.LocalDate;

public class Order extends Data.Editable {
    public final LocalDate registrationDate;
    public final LocalDate requiredDate;
    public final Customer customerInfo;
    public final ProductType productType;
    public final int quantity;
    public final String additionalInfo;
    public final OrderStatus status;

    // Конструктор с проверкой
    public Order(
            LocalDate registrationDate,
            LocalDate requiredDate,
            Customer customerInfo,
            ProductType productType,
            Integer quantity,
            String additionalInfo,
            OrderStatus status
    ) throws Exception {
        super(productType.name);

        //Логика такая: ты берешь данные заказа не проверяя их регистрируешь,
        //Если выброшено исключение выводишь его сообщение
        //exc.getMessage()

        //Условия валидности
        if (!requiredDate.isAfter(registrationDate)) {
            throw new Exception("Неправильно указанна дата");
        }
        if (status == OrderStatus.APPROVED &&
                (customerInfo == null || quantity == null)) {
            throw new Exception("Заказ не может быть согласован если " +
                    "не предоставлена информация о клиенте и количество продукции");
        }

        if (status == null || status == OrderStatus.DEFAULT) {
            status = OrderStatus.DRAFT; // Статус по умолчанию
        }
        this.registrationDate = registrationDate;
        this.requiredDate = requiredDate;
        this.customerInfo = customerInfo;
        this.productType = productType;
        this.quantity = quantity;
        this.additionalInfo = additionalInfo;
        this.status = status;
    }

}
