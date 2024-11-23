package com.application.workshop.timber;

import com.application.editor.EditorEntry;
import com.application.order.Order;
import com.application.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public class TimberProductOrder {
    @EditorEntry
    public LocalDate registrationDate;
    @EditorEntry
    public LocalDate startDate;
    @EditorEntry
    public String productType;
    @EditorEntry
    public Order order;
    @EditorEntry
    public int quantity;
    @EditorEntry
    public List<String> productionWorkshops;
    @EditorEntry
    public String additionalInfo;

    public TimberProductOrder(LocalDate registrationDate, LocalDate startDate,
                              Order order, String productType,
                              int quantity, List<String> productionWorkshops,
                              String additionalInfo) {
        validateFields(registrationDate, startDate, order, productType, quantity, productionWorkshops);
        this.registrationDate = registrationDate;
        this.startDate = startDate;
        this.order = order;
        this.productType = productType;
        this.quantity = quantity;
        this.productionWorkshops = productionWorkshops;
        this.additionalInfo = additionalInfo;

        //ТЗ:
        this.order.status = OrderStatus.IN_PRODUCTION;
    }

    private void validateFields(LocalDate registrationDate, LocalDate startDate,
                                Order order, String productType,
                                int quantity, List<String> productionWorkshops) {

        //Оставил все условия из тз чтобы легче было рефакторить
        if (registrationDate == null || startDate == null) {
            throw new IllegalArgumentException("Дата регистрации и дата начала производства не могут быть null.");
        }
        if (!startDate.isAfter(registrationDate)) {
            throw new IllegalArgumentException("Дата начала производства должна быть позже даты регистрации.");
        }
        if (order == null || OrderStatus.APPROVED != order.status) {
            throw new IllegalArgumentException("Задание на производство можно зарегистрировать только по заказу со статусом 'Согласовано клиентом'.");
        }
        if (productType == null || productType.isEmpty()) {
            throw new IllegalArgumentException("Вид лесопродукции должен быть указан.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество лесопродукции должно быть больше нуля.");
        }
        if (productionWorkshops == null || productionWorkshops.isEmpty()) {
            throw new IllegalArgumentException("Необходимо указать цеха для изготовления лесопродукции.");
        }
    }
}