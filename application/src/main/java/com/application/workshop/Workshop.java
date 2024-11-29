package com.application.workshop;

import com.futurefactory.Data.Editable;
import com.futurefactory.editor.EditorEntry;

public class Workshop extends Editable{
	@EditorEntry(translation="рабочие участки",editorBaseSource=WorkArea.WorkAreaListEditor.class)
	public final WorkArea[]parts;
	public Workshop(String name,WorkArea[]workShopParts){
		super(name);
		this.parts=workShopParts;
	}

	@Override
	public String toString() {
		return name;
	}
}
