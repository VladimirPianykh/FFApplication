package com.application.editor;

import com.futurefactory.Data;

import java.awt.*;
import java.lang.reflect.Field;

public interface EditorEntryBase {
    Component createEditorBase(Data.Editable o, Field f);
}
