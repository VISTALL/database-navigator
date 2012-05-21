package com.dci.intellij.dbn.language.common.navigation;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class NavigationGutterRenderer extends GutterIconRenderer {
    private NavigationAction action;
    public NavigationGutterRenderer(NavigationAction action) {
        this.action = action;
    }

    @NotNull
    public Icon getIcon() {
        return action.getTemplatePresentation().getIcon();
    }

    public boolean isNavigateAction() {
        return true;
    }

    @Nullable
    public synchronized AnAction getClickAction() {
        return action;
    }

    @Nullable
    public String getTooltipText() {
        return action.getTemplatePresentation().getText();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NavigationGutterRenderer) {
            NavigationGutterRenderer renderer = (NavigationGutterRenderer) obj;
            return action.equals(renderer.action);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return action.hashCode();
    }
}