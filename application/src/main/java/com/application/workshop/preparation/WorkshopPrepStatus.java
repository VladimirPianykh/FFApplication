package com.application.workshop.preparation;

public enum WorkshopPrepStatus {
    CREATED("Создан"),
    COMPLETED("Выполнен");


    public final String translation;

    WorkshopPrepStatus(String translation) {
        this.translation=translation;
    }

    @Override
    public String toString(){return translation;}
}
