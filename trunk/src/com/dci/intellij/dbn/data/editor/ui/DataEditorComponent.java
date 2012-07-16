package com.dci.intellij.dbn.data.editor.ui;

import javax.swing.JTextField;

public interface DataEditorComponent {
    JTextField getTextField();

    void setEditable(boolean editable);

    void setEnabled(boolean enabled);

    UserValueHolder getUserValueHolder();

    void setUserValueHolder(UserValueHolder userValueHolder);

    String getText();

    void setText(String text);
}
