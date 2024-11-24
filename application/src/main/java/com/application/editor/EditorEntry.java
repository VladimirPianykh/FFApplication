package com.application.editor;

import java.awt.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
public @interface EditorEntry{
    String translation();
    Class<? extends EditorEntryBase> editorBaseSource() default EditorEntryBase.class;
}
