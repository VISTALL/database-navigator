package com.dci.intellij.dbn.language.common.element;

import com.dci.intellij.dbn.code.common.lookup.LookupItemFactory;
import com.dci.intellij.dbn.language.common.DBLanguage;

public interface TokenElementType extends LeafElementType {
    String SEPARATOR = "SEPARATOR";

    boolean isCharacter();

    LookupItemFactory getLookupItemFactory(DBLanguage language);
}
