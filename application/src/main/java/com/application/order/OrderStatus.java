package com.application.order;

// Перечисление для статуса заказа
enum OrderStatus {
    DRAFT, // Черновик
    APPROVED, // Согласован клиентом
    IN_PRODUCTION, // Принят в производство
    COMPLETED, // Выполнен
    DEFAULT;
}