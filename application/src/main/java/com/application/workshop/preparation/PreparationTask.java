package com.application.workshop.preparation;

import com.futurefactory.Data;
import com.futurefactory.editor.EditorEntry;
import com.application.workshop.WorkArea;
import com.application.workshop.timber.TimberProductTask;
import com.futurefactory.editor.VerifiedInput;
import com.futurefactory.editor.Verifier;

import java.lang.annotation.Inherited;
import java.time.LocalDate;

@VerifiedInput(verifier = PreparationTask.TaskVerifier.class)
public class PreparationTask extends Data.Editable{
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

	@Override
	public String toString() {
		return name;
	}

	public static class TaskVerifier implements Verifier {
		@Override
		public String verify(Data.Editable editable, boolean isNew) {
			PreparationTask task = (PreparationTask) editable;
			if(task.registrationDate==null||task.preparationDate==null)
				return "Дата регистрации и дата подготовки не могут быть null.";
			if(task.productionOrder==null)
				return "Задание на подготовку должно быть связано с заданием на производство.";
			if(task.workArea==null||task.workArea.name.isEmpty())
				return "Рабочий участок должен быть указан.";
			if(!task.preparationDate.isBefore(task.productionOrder.startDate))
				return "Подготовка участка должна быть выполнена до начала изготовления продукции.";
			return "";
		}
	}
}