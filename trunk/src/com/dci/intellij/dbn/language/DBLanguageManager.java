package com.dci.intellij.dbn.language;

import com.dci.intellij.dbn.language.common.DBLanguageInjector;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class DBLanguageManager extends AbstractProjectComponent{
    private DBLanguageManager(Project project) {
        super(project);
    }

    public static DBLanguageManager getInstance(Project project) {
        return project.getComponent(DBLanguageManager.class);
    }

    public void projectOpened() {

    }

    public void projectClosed() {
    }

    @NotNull
    public String getComponentName() {
        return "DBNavigator.Project.LanguageManager";
    }

    public void initComponent() {
        //PsiManager.getInstance(myProject).registerLanguageInjector(new DBLanguageInjector());
    }

    public void disposeComponent() {
    }
}
