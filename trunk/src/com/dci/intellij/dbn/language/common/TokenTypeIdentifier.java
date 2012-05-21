package com.dci.intellij.dbn.language.common;

public enum TokenTypeIdentifier {
    UNKNOWN("unknown"),
    KEYWORD("keyword"),
    FUNCTION("function"),
    PARAMETER("parameter"),
    DATATYPE("datatype"),
    EXCEPTION("exception"),
    OPERATOR("operator"),
    CHARACTER("character"),
    IDENTIFIER("identifier"),
    CHAMELEON("chameleon");

    private String name;
    TokenTypeIdentifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TokenTypeIdentifier getIdentifier(String typeName) {
        for (TokenTypeIdentifier identifier : TokenTypeIdentifier.values()) {
            if (identifier.getName().equals(typeName)) return identifier;
        }
        return UNKNOWN;
    }
}
