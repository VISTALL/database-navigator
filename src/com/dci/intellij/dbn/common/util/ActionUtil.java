package com.dci.intellij.dbn.common.util;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

public class ActionUtil {
    public static final AnAction SEPARATOR = new AnAction() {
        public void actionPerformed(AnActionEvent e) {}
    };


    public static ActionToolbar createActionToolbar(String place, boolean horizontal, String actionGroupName){
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup actionGroup = (ActionGroup) actionManager.getAction(actionGroupName);
        return actionManager.createActionToolbar(place, actionGroup, horizontal);
    }

    public static ActionToolbar createActionToolbar(String place, boolean horizontal, ActionGroup actionGroup){
        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar(place, actionGroup, horizontal);
    }

    public static ActionToolbar createActionToolbar(String place, boolean horizontal, AnAction... actions){
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (AnAction action : actions) {
            if (action == SEPARATOR)
                actionGroup.addSeparator(); else
                actionGroup.add(action);
        }

        return actionManager.createActionToolbar(place, actionGroup, horizontal);
    }

    public static Project getProject(AnActionEvent e) {
        return e.getData(PlatformDataKeys.PROJECT);
    }

    /**
     * @deprecated use getProject(Component)
     */
    public static Project getProject(){
        return PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
    }

    public static Project getProject(Component component){
        return PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(component));
    }
    
    public static void registerDataProvider(JComponent component, DataProvider dataProvider, boolean recursive) {
        DataManager.registerDataProvider(component, dataProvider);
        if (recursive) {
            for (Component child : component.getComponents()) {
                if (child instanceof JComponent) {
                    JComponent childComponent = (JComponent) child;
                    registerDataProvider(childComponent, dataProvider, recursive);
                }
            }
        }
    }
    
    public static DataProvider getDataProvider(JComponent component) {
        if (component != null) {
            DataProvider dataProvider = DataManager.getDataProvider(component);
            if (dataProvider == null) {
                JComponent parent = (JComponent) component.getParent();
                return getDataProvider(parent);
            } else {
                return dataProvider;
            }
        }
        return null;
    }

}
