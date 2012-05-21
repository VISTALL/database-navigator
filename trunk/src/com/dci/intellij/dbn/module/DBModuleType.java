package com.dci.intellij.dbn.module;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;

public class DBModuleType<T extends DBModuleBuilder> extends ModuleType<T> {
    public static final DBModuleType MODULE_TYPE = new DBModuleType("DBNavigator.ModuleType.DatabaseModule");
    protected DBModuleType(String id) {
        super(id);
    }

    public T createModuleBuilder() {
        return (T) new DBModuleBuilder();
    }

    public String getName() {
        return "Database module";
    }

    public String getDescription() {
        return "<b>Database module</b> is a part of a project which bundles all database related objects like ddl/dml scripts, database connections and so on...";
    }

    public Icon getBigIcon() {
        return Icons.DATABASE_MODULE;
    }

    public Icon getNodeIcon(boolean isOpened) {
        return isOpened ? Icons.DATABASE_MODULE_SMALL_OPEN : Icons.DATABASE_MODULE_SMALL_CLOSED;
    }
    public ModuleWizardStep[] createWizardSteps(WizardContext wizardContext, T moduleBuilder, ModulesProvider modulesProvider) {
        List<ModuleWizardStep> wizardSteps = new ArrayList<ModuleWizardStep>();

        //ProjectWizardStepFactory.getInstance().createNameAndLocationStep(wizardContext);
        wizardSteps.add(new DBModuleConnectionsWizardStep(wizardContext, moduleBuilder, modulesProvider));
        return wizardSteps.toArray(new ModuleWizardStep[wizardSteps.size()]);
    }


}
