package com.application.workshop.preparation;

import com.application.editor.EditorEntry;
import com.application.workshop.WorkArea;
import com.application.workshop.timber.TimberProductTask;

import java.time.LocalDate;

public class PreparationOrder {

    @EditorEntry
    public LocalDate registrationDate;
    @EditorEntry
    public LocalDate preparationDate;
    @EditorEntry
    public TimberProductTask productionTask;
    @EditorEntry
    public WorkArea workArea;
    @EditorEntry
    public String preparationDetails;

    /*
    Статус задания («Создано» и «Выполнено»). Объект создается изначально в статусе "Создан"
    //TODO: Определиться как быть со статусом, при первом создании убирать поле статуса, или оставить всё как есть
     */
    @EditorEntry
    public WorkshopPrepStatus status;

    // Constructor
    public PreparationOrder(LocalDate registrationDate,
                            LocalDate preparationDate,
                            TimberProductTask productionTask,
                            WorkArea workArea,
                            String preparationDetails,
                            WorkshopPrepStatus status
    ) {
        validateFields(registrationDate, preparationDate, productionTask, workArea);
        this.registrationDate = registrationDate;
        this.preparationDate = preparationDate;
        this.productionTask = productionTask;
        this.workArea = workArea;
        this.preparationDetails = preparationDetails;

        //TODO: выше
        this.status = status;
    }

    private void validateFields(LocalDate registrationDate, LocalDate preparationDate,
                                TimberProductTask productionOrder, WorkArea workArea) {

        //Оставил все условия из тз чтобы легче было рефакторить
        if (registrationDate == null || preparationDate == null) {
            throw new IllegalArgumentException("Дата регистрации и дата подготовки не могут быть null.");
        }
        if (productionOrder == null) {
            throw new IllegalArgumentException("Задание на подготовку должно быть связано с заданием на производство.");
        }
        if (workArea == null || workArea.name.isEmpty()) {
            throw new IllegalArgumentException("Рабочий участок должен быть указан.");
        }
        if (!preparationDate.isBefore(productionOrder.startDate)) {
            throw new IllegalArgumentException("Подготовка участка должна быть выполнена до начала изготовления продукции.");
        }
    }
}