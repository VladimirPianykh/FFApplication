package com.application.workshop.preparation;

import com.application.order.Order;
import com.futurefactory.Data;
import com.futurefactory.editor.EditorEntry;
import com.application.workshop.WorkArea;
import com.application.workshop.timber.TimberProductTask;
import com.futurefactory.editor.VerifiedInput;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@VerifiedInput(verifier= PreparationTask.Verifier.class)
public class PreparationTask extends Data.Editable {
	public static class Verifier implements com.futurefactory.editor.Verifier{
		@Override
		public boolean verify(Data.Editable editable){PreparationTask e=(PreparationTask)editable;return true;}
	}

	@EditorEntry(translation="Дата регистрация")
	public LocalDate registrationDate;
	@EditorEntry(translation="Дата окончания")
	public LocalDate preparationDate;
	@EditorEntry(translation="Задание на производство")
	public TimberProductTask productionOrder;
	@EditorEntry(translation="Рабочий участок", editorBaseSource = WorkArea.WorkAreaListEditor.class)
	public WorkArea workArea;
	@EditorEntry(translation="Дополнительная информация")
	public String preparationDetails;
	@EditorEntry(translation="Статус задания")
	public WorkshopPrepStatus status;
	// Constructor
	public PreparationTask(LocalDate preparationDate,
							TimberProductTask productionOrder,
							WorkArea workArea,
							String preparationDetails,
							WorkshopPrepStatus status
	){
		super("Задание на обработку участка");
		validateFields(registrationDate,preparationDate,productionOrder,workArea);
		this.registrationDate=LocalDate.now();
		this.preparationDate=preparationDate;
		this.productionOrder=productionOrder;
		this.workArea=workArea;
		this.preparationDetails=preparationDetails;
		this.status=status;
	}

	public PreparationTask() {
        super("Задание на обработку участка");
		this.registrationDate=LocalDate.now();
		this.preparationDate= LocalDate.now().plusDays(3);
		this.productionOrder=null;
		this.workArea=null;
		this.preparationDetails="";
		this.status=WorkshopPrepStatus.CREATED;
	}
	private boolean validateFields(LocalDate registrationDate,LocalDate preparationDate,
								TimberProductTask productionOrder,WorkArea workArea){
		//Оставил все условия из тз чтобы легче было рефакторить
		if(registrationDate==null||preparationDate==null)return false;
			// throw new IllegalArgumentException("Дата регистрации и дата подготовки не могут быть null.");
		if(productionOrder==null)return false;
			// throw new IllegalArgumentException("Задание на подготовку должно быть связано с заданием на производство.");
		if(workArea==null||workArea.name.isEmpty())return false;
			// throw new IllegalArgumentException("Рабочий участок должен быть указан.");
		if(!preparationDate.isBefore(productionOrder.startDate))return false;
			// throw new IllegalArgumentException("Подготовка участка должна быть выполнена до начала изготовления продукции.");
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}