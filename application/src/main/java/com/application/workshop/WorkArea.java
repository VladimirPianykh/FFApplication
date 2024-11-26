package com.application.workshop;

import com.futurefactory.Data.Editable;
import com.futurefactory.editor.EditorEntry;

public class WorkArea extends Editable{
    @EditorEntry(translation="Информация")
    public String description;
    public WorkArea(String name){super(name);}
    public boolean equals(Object o){
        return (o instanceof WorkArea&&((WorkArea)o).name.equals(name));
    }
}
