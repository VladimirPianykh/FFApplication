package com.application.editor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EditorEntry{
    String translation();
    Class<? extends EditorEntryBase> editorBaseSource() default EditorEntryBase.class;
}
