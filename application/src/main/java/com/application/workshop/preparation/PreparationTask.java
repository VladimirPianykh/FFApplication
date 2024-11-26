package com.application.workshop.preparation;

import com.futurefactory.editor.EditorEntry;
import com.application.workshop.WorkArea;
import com.application.workshop.timber.TimberProductTask;

import java.io.Serializable;
import java.time.LocalDate;

public class PreparationTask implements Serializable {

	@EditorEntry(translation = "Дата регистрация")
	public LocalDate registrationDate;
	@EditorEntry(translation = "Дата окончания")
	public LocalDate preparationDate;
	@EditorEntry(translation = "Задание на производство")
	public TimberProductTask productionOrder;
	@EditorEntry(translation = "Рабочий участок")
	public WorkArea workArea;
	@EditorEntry(translation = "Дополнительная информация")
	public String preparationDetails;

	/*
	Статус задания («Создано» и «Выполнено»). Объект создается изначально в статусе "Создан"
	//TODO: Определиться как быть со статусом, при первом создании убирать поле статуса, или оставить всё как есть
	 */
	@EditorEntry(translation = "Статус задания")
	public WorkshopPrepStatus status;

	// Constructor
	public PreparationTask(LocalDate registrationDate,
							LocalDate preparationDate,
							TimberProductTask productionOrder,
							WorkArea workArea,
							String preparationDetails,
							WorkshopPrepStatus status
	) {
		validateFields(registrationDate, preparationDate, productionOrder, workArea);
		this.registrationDate = registrationDate;
		this.preparationDate = preparationDate;
		this.productionOrder = productionOrder;
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