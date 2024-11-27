package com.application.workshop;

import com.application.workshop.manager.WorkAreaManager;
import com.futurefactory.Data;
import com.futurefactory.Data.Editable;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.EditorEntryBase;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WorkArea extends Editable{

    public static class WorkAreaListEditor implements EditorEntryBase {
        @SuppressWarnings("unchecked")
        public Component createEditorBase(Editable o, Field f){
            JPanel p=new JPanel(new GridLayout(1,0));
            var areas = WorkAreaManager.getAreas();
            WorkArea[] workAreasArr = new WorkArea[areas.size()];
            for(int i = 0; i < areas.size(); i++) {
                workAreasArr[i] = areas.get(i);
            }
            JComboBox<WorkArea> areaJComboBox = new JComboBox<>(workAreasArr);
            areaJComboBox.addActionListener(e->{
                try{f.set(o,areaJComboBox.getSelectedItem());}
                catch(IllegalAccessException ex){throw new RuntimeException(ex);}
            });
            p.add(areaJComboBox);
            return p;
        }
    }

    @EditorEntry(translation="Информация")
    public String description;
    public WorkArea(String name){
        super(name);
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
