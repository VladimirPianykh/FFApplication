package com.application.workshop;

import com.application.workshop.manager.WorkAreaManager;
import com.futurefactory.Data.Editable;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.EditorEntryBase;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class WorkArea extends Editable{
	public static class WorkAreaListEditor implements EditorEntryBase{
		public Component createEditorBase(Editable o,Field f){
			JPanel p=new JPanel(new GridLayout(1,0));
			var areas=WorkAreaManager.getAreas();
			WorkArea[]workAreasArr=new WorkArea[areas.size()];
			for(int i=0;i<areas.size();i++)workAreasArr[i]=areas.get(i);
			JComboBox<WorkArea>areaJComboBox=new JComboBox<>(workAreasArr);
			areaJComboBox.addActionListener(e->{
				try{f.set(o,new WorkArea[]{(WorkArea)areaJComboBox.getSelectedItem()});}
				catch(IllegalAccessException ex){throw new RuntimeException(ex);}
			});
			p.add(areaJComboBox);
			return p;
		}
	}

	//TODO: разобраться не забыли ли об этом поле(нигде сейчас не используется)
	@EditorEntry(translation="Информация")
	public String description;
	@EditorEntry(translation="Производительность")
    public int performance;
    public WorkArea(String name,int performance){
        super(name);
        this.performance=performance;
        WorkAreaManager.registerArea(this);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(Object o){
        return (o instanceof WorkArea&&((WorkArea)o).name.equals(name));
    }
}
