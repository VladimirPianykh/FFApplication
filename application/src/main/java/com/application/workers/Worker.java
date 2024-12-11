package com.application.workers;

import com.application.workshop.Workshop;
import com.futurefactory.Data.Editable;
import com.futurefactory.editor.EditorEntry;

public class Worker extends Editable{
	@EditorEntry(translation="Цех")
	public Workshop workshop;
	public Worker(){super("Новый сотрудник");}
}
