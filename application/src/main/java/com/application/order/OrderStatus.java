package com.application.order;

// Перечисление для статуса заказа
//Чтобы не писать кучу if-else просто выводишь status.toString() или status.toRussian
enum OrderStatus {
    DRAFT("Черновик"),
    APPROVED("Согласован клиентом"),
    IN_PRODUCTION("Принят в производство"),
    COMPLETED("Выполнен"),
    // не будет использоваться, его автоматически заменят на Черновик
    DEFAULT("По умолчанию");
    public final String toRussian;

    OrderStatus(String translation) {
        toRussian = translation;
    }

    @Override
    public String toString() {
        return toRussian;
    }
}