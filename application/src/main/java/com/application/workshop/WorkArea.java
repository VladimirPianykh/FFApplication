package com.application.workshop;

import com.application.editor.EditorEntry;

import java.io.Serializable;

public class WorkArea implements Serializable {
    @EditorEntry(translation = "Название")
    public String name;

    public WorkArea(String name) {
        this.name = name;
    }
}
