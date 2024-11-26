package com.application.workshop.timber;

import com.futurefactory.editor.EditorEntry;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.futurefactory.Data;

import java.time.LocalDate;
import java.util.List;

public class TimberProductTask extends Data.Editable {
    @EditorEntry(translation = "Дата регистрации задания")
    public LocalDate registrationDate;
    @EditorEntry(translation = "Дата, с которой требуется начать выполнять задание")
    public LocalDate startDate;
    @EditorEntry(translation = "Вид лесопродукции")
    public String productType;
    @EditorEntry(translation = "Заказ на лесопродукцию")
    public Order order;
    @EditorEntry(translation = "Количество лесопродукции")
    public int quantity;
    @EditorEntry(translation = "Цеха, которые будут задействованы в изготовлении лесопродукции")
    public List<String> productionWorkshops;
    @EditorEntry(translation = "Дополнительная информация")
    public String additionalInfo;

    public TimberProductTask(LocalDate registrationDate, LocalDate startDate,
                             Order order, String productType,
                             int quantity, List<String> productionWorkshops,
                             String additionalInfo) {
        super("Редактирование");
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