package com.application.workers;

import com.application.workshop.Workshop;
import com.futurefactory.Data.Editable;
import com.futurefactory.defaults.editorbases.NullVerifier;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.VerifiedInput;

@VerifiedInput(verifier = NullVerifier.class)
public class Worker extends Editable{
	@EditorEntry(translation="Цех")
	public Workshop workshop;
	public Worker(){super("Новый сотрудник");}
}
