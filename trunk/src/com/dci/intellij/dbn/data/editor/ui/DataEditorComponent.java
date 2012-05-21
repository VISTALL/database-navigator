package com.dci.intellij.dbn.data.editor.ui;

import javax.swing.JTextField;

public interface DataEditorComponent {
    JTextField getTextField();

    void setEditable(boolean editable);

    void setUserValueHolder(UserValueHolder userValueHolder);

    void setEnabled(boolean enabled);

    UserValueHolder getUserValueHolder();
}
