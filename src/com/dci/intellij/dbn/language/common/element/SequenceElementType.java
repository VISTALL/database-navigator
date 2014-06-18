package com.dci.intellij.dbn.language.common.element;

import com.dci.intellij.dbn.language.common.TokenType;

import java.util.Set;

public interface SequenceElementType extends DBNElementType {
    DBNElementType[] getElementTypes();

    int elementsCount();

    boolean isOptionalFromIndex(int index);

    boolean isLast(int index);

    boolean isFirst(int index);

    boolean isOptional(int index);

    boolean isOptional(DBNElementType elementType);

    boolean canStartWithElement(DBNElementType elementType);

    boolean shouldStartWithElement(DBNElementType elementType);

    boolean isExitIndex(int index);

    boolean containsLandmarkTokenFromIndex(TokenType tokenType, int index);

    Set<TokenType> getFirstPossibleTokensFromIndex(int index);

    boolean isPossibleTokenFromIndex(TokenType tokenType, int index);

    int indexOf(DBNElementType elementType, int fromIndex);

    int indexOf(DBNElementType elementType);

    boolean[] getOptionalElementsMap();
}
