package com.application.workshop;

import com.futurefactory.Data.Editable;
import com.futurefactory.Wrapper;
import com.futurefactory.defaults.editorbases.NullVerifier;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.EditorEntryBase;
import com.futurefactory.editor.VerifiedInput;
import com.futurefactory.editor.Verifier;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class Workshop extends Editable{
	public static class WorkshopListEditor implements EditorEntryBase {
		public JComponent createEditorBase(Object o, Field f, Wrapper<Runnable> saver){
			JPanel p=new JPanel(new GridLayout(1,0));
			JList<WorkArea> workAreaJList = new JList<>(((Workshop) o).parts);
			p.add(workAreaJList);
			saver.var = () -> {};
			return p;
		}
	}

	@EditorEntry(translation="рабочие участки",editorBaseSource=WorkshopListEditor.class)
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
