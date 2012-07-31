package com.dci.intellij.dbn.editor.data.ui.table.model;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface DatasetEditorModelCellValueListener extends EventListener {
    Topic<DatasetEditorModelCellValueListener> TOPIC = Topic.create("Cell value listener", DatasetEditorModelCellValueListener.class);
    void valueChanged(DatasetEditorModelCell cell);
}
