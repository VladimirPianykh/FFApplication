package com.application.workshop;

import com.application.editor.EditorEntry;

public class WorkArea {
    @EditorEntry
    public String name;

    public WorkArea(String name) {
        this.name = name;
    }
}
