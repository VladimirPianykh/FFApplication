package com.application.order;

// Перечисление для статуса заказа
//Чтобы не писать кучу if-else просто выводишь status.toString() или status.translation
public enum OrderStatus{
    DRAFT("Черновик"),
    APPROVED("Согласован клиентом"),
    IN_PRODUCTION("Принят в производство"),
    COMPLETED("Выполнен");
    public final String translation;

    OrderStatus(String translation) {
        this.translation=translation;
    }

    @Override
    public String toString(){return translation;}
}