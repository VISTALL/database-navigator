package com.dci.intellij.dbn.common;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.RowIcon;
import gnu.trove.THashMap;

import javax.swing.*;
import java.util.Map;

public class Icons {
    private static final Map<String, Icon> REGISTERED_ICONS = new THashMap<String, Icon>();

    public static final Icon COMMON_INFO = load("/img/v0/common/Info.png");
    public static final Icon COMMON_INFO_DISABLED = load("/img/v0/common/InfoDisabled.png");
    public static final Icon COMMON_WARNING = load("/img/v0/common/WarningTriangle.png");
    public static final Icon COMMON_RIGHT = load("/img/v0/common/SplitRight.png");
    public static final Icon COMMON_UP = load("/img/v0/common/SplitUp.png");


    public static final Icon ACTION_COPY = load("/img/v0/action/Copy.png");
    public static final Icon ACTION_SORT_ALPHA = load("/img/v0/action/SortAlphabetically.png");
    public static final Icon ACTION_SORT_ASC = load("/img/v0/action/SortAscending.png");
    public static final Icon ACTION_SORT_DESC = load("/img/v0/action/SortDescending.png");
    public static final Icon ACTION_ADD = load("/img/v0/action/Add.png");
    public static final Icon ACTION_ADD_SPECIAL = load("/img/v0/action/AddSpecial.png");
    public static final Icon ACTION_REMOVE = load("/img/v0/action/Remove.png");
    public static final Icon ACTION_MOVE_UP = load("/img/v0/action/MoveUp.png");
    public static final Icon ACTION_MOVE_DOWN = load("/img/v0/action/MoveDown.png");
    public static final Icon ACTION_EDIT = load("/img/v0/action/EditSource.png");
    public static final Icon ACTION_SETTINGS = load("/img/v0/action/Properties.png");
    public static final Icon ACTION_COLLAPSE_ALL = load("/img/v0/action/CollapseAll.png");
    public static final Icon ACTION_EXPAND_ALL = load("/img/v0/action/ExpandAll.png");
    public static final Icon ACTION_GROUP = load("/img/v0/action/Group.png");
    public static final Icon ACTION_DELETE = load("/img/v0/action/Delete.png");
    public static final Icon ACTION_CLOSE = ACTION_DELETE;
    public static final Icon ACTION_UP_DOWN = load("/img/v0/action/UpDown.png");
    public static final Icon ACTION_REFRESH = load("/img/v0/action/Synchronize.png");
    public static final Icon ACTION_FIND = load("/img/v0/action/Find.png");
    public static final Icon ACTION_WRAP_TEXT = load("/img/v0/action/WrapText.png");
    public static final Icon ACTION_RERUN = load("/img/v0/action/Rerun.png");
    public static final Icon ACTION_PIN = load("/img/v0/action/Pin.png");
    public static final Icon ACTION_REVERT_CHANGES = load("/img/v0/action/RevertChanges.png");


    public static final Icon DATABASE_NAVIGATOR = load("/img/v0/project/DatabaseNavigator.png");
    public static final Icon DATABASE_MODULE = load("/img/v0/project/DatabaseModule.png");
    public static final Icon DATABASE_MODULE_SMALL_OPEN = load("/img/v0/project/DatabaseModuleOpen.png");
    public static final Icon DATABASE_MODULE_SMALL_CLOSED = load("/img/v0/project/DatabaseModuleClosed.png");

    public static final Icon WINDOW_DATABASE_BROWSER = load("/img/v0/window/DatabaseBrowser.png");
    public static final Icon WINDOW_EXECUTION_CONSOLE = load("/img/v0/window/ExecutionConsole.png");

    public static final Icon FILE_SQL_CONSOLE = load("/img/v0/file/SQLConsole.png");
    public static final Icon FILE_SQL = load("/img/v0/file/SQLFile.png");
    public static final Icon FILE_PLSQL = load("/img/v0/file/PLSQLFile.png");
    public static final Icon FILE_BLOCK_PLSQL = load("FILE_BLOCK_PLSQL", "/img/v0/PLSQLBlock.png");
    public static final Icon FILE_BLOCK_PSQL = load("FILE_BLOCK_PSQL", "/img/v0/file/PSQLBlock.png");
    public static final Icon FILE_BLOCK_SQL = load("FILE_BLOCK_SQL", "/img/v0/file/SQLBlock.png");


    public static final Icon DIALOG_INFO     = load("/img/v0/dialog/Information.png");
    public static final Icon DIALOG_WARNING  = load("/img/v0/dialog/Warning.png");
    public static final Icon DIALOG_ERROR    = load("/img/v0/dialog/Error.png");
    public static final Icon DIALOG_QUESTION = load("/img/v0/dialog/Dialog.png");


    public static final Icon METHOD_EXECUTION_RUN     = load("/img/v0/action/ExecuteMethod.png");
    public static final Icon METHOD_EXECUTION_DEBUG   = load("/img/v0/action/DebugMethod.png");
    public static final Icon METHOD_EXECUTION_RERUN   = load("/img/v0/RerunMethodExecution.png");
    public static final Icon METHOD_EXECUTION_DIALOG  = load("/img/v0/ExecuteMethodDialog.png");
    public static final Icon METHOD_EXECUTION_HISTORY = load("/img/v0/MethodExecutionHistory.png");
    public static final Icon METHOD_LOOKUP            = load("/img/v0/MethodLookup.png");


    public static final Icon STMT_EXECUTION_RUN     = load("/img/v0/action/ExecuteStatement.png");
    public static final Icon STMT_EXECUTION_RERUN   = load("/img/v0/action/Rerun.png");
    public static final Icon STMT_EXECUTION_RESUME  = load("/img/v0/action/ResumeExecution.png");
    public static final Icon STMT_EXECUTION_REFRESH = load("/img/v0/action/Refresh.png");
    public static final Icon STMT_EXECUTION_ERROR   = load("/img/v0/common/Error.png");

    public static final Icon STMT_EXEC_RESULTSET        = load("/img/v0/ExecutionResultSet.png");
    public static final Icon STMT_EXEC_RESULTSET_RERUN  = load("/img/v0/ExecutionResultSetRerun.png");
    public static final Icon STMT_EXEC_RESULTSET_ORPHAN = load("/img/v0/ExecutionResultSetOrphan.png");

    public static final Icon EXEC_RESULT_RERUN            = load("/img/v0/action/Refresh.png");
    public static final Icon EXEC_RESULT_OPEN_EXEC_DIALOG = load("/img/v0/ExecuteMethodDialog.png");
    public static final Icon EXEC_RESULT_RESUME           = load("/img/v0/action/ResumeExecution.png");
    public static final Icon EXEC_RESULT_STOP             = load("/img/v0/action/StopExecution.png");
    public static final Icon EXEC_RESULT_CLOSE            = load("/img/v0/action/Close.png");
    public static final Icon EXEC_RESULT_VIEW_STATEMENT   = load("/img/v0/action/Preview.png");
    public static final Icon EXEC_RESULT_VIEW_RECORD      = load("/img/v0/RecordViewer.png");
    public static final Icon EXEC_RESULT_OPTIONS          = load("/img/v0/action/Properties.png");
    public static final Icon EXEC_RESULT_MESSAGES         = load("/img/v0/common/Messages.png");
    public static final Icon EXEC_CONFIG                  = load("/img/v0/DBProgram.png");

    public static final Icon NAVIGATION_GO_TO_SPEC       = load("/img/v0/GoToSpec.png");
    public static final Icon NAVIGATION_GO_TO_BODY       = load("/img/v0/GoToBody.png");

    public static final Icon BROWSER_BACK = load("/img/v0/action/BrowserBack.png");
    public static final Icon BROWSER_NEXT = load("/img/v0/action/BrowserForward.png");
    public static final Icon BROWSER_AUTOSCROLL_TO_EDITOR = load("/img/v0/action/AutoscrollToSource.png");
    public static final Icon BROWSER_AUTOSCROLL_FROM_EDITOR = load("/img/v0/action/AutoscrollFromSource.png");
    public static final Icon BROWSER_OBJECT_PROPERTIES = load("/img/v0/ObjectProperties.png");


    public static final Icon DATA_EDITOR_ROW_INSERT = load("/img/v0/InsertRow.png");
    public static final Icon DATA_EDITOR_ROW_NEW = load("/img/v0/NewRow.png");
    public static final Icon DATA_EDITOR_ROW_MODIFIED = load("/img/v0/ModifiedRow.png");
    public static final Icon DATA_EDITOR_ROW_DELETED = load("/img/v0/DeletedRow.png");

    public static final Icon DATA_EDITOR_DUPLICATE_RECORD = load("/img/v0/action/DuplicateRecord.png");
    public static final Icon DATA_EDITOR_INSERT_RECORD = load("/img/v0/action/InsertRecord.png");
    public static final Icon DATA_EDITOR_DELETE_RECORD = load("/img/v0/action/DeleteRecord.png");
    public static final Icon DATA_EDITOR_SWITCH_EDITABLE_STATUS = load("/img/v0/DatasetEditorSwitchEditableStatus.png");
    public static final Icon DATA_EDITOR_FETCH_NEXT_RECORDS = load("/img/v0/action/ResumeExecution.png");
    public static final Icon DATA_EDITOR_EDIT_RECORD = load("/img/v0/EditDatasetRecord.png");
    public static final Icon DATA_EDITOR_NEXT_RECORD = load("/img/v0/NextRecord.png");
    public static final Icon DATA_EDITOR_PREVIOUS_RECORD = load("/img/v0/PreviousRecord.png");
    public static final Icon DATA_EDITOR_FIRST_RECORD = load("/img/v0/FirstRecord.png");
    public static final Icon DATA_EDITOR_LAST_RECORD = load("/img/v0/LastRecord.png");
    public static final Icon DATA_EDITOR_LOCK_EDITING = load("/img/v0/LockEditing.png");

    public static final Icon DATA_EDITOR_RELOAD_DATA = load("/img/v0/action/Refresh.png");
    public static final Icon DATA_EDITOR_BROWSE =    load("/img/v0/Browse.png");
    public static final Icon DATA_EDITOR_CALENDAR =    load("/img/v0/Calendar.png");

    public static final Icon DATA_EXPORT =    load("/img/v0/action/DataExport.png");
    public static final Icon DATA_IMPORT =    load("/img/v0/action/DataImport.png");

    public static final Icon DATASET_FILTER =    load("/img/v0/filter/DatasetFilter.png");
    public static final Icon DATASET_FILTER_NEW =    load("/img/v0/filter/DatasetFilterNew.png");
    public static final Icon DATASET_FILTER_EDIT =    load("/img/v0/filter/DatasetFilterEdit.png");
    public static final Icon DATASET_FILTER_BASIC =    load("/img/v0/filter/DatasetFilterBasic.png");
    public static final Icon DATASET_FILTER_BASIC_ERR =    load("/img/v0/filter/DatasetFilterBasicErr.png");
    public static final Icon DATASET_FILTER_BASIC_TEMP =    load("/img/v0/filter/DatasetFilterBasicTemp.png");
    public static final Icon DATASET_FILTER_BASIC_TEMP_ERR =    load("/img/v0/filter/DatasetFilterBasicTempErr.png");
    public static final Icon DATASET_FILTER_CUSTOM =    load("/img/v0/filter/DatasetFilterCustom.png");
    public static final Icon DATASET_FILTER_CUSTOM_ERR =    load("/img/v0/filter/DatasetFilterCustomErr.png");
    public static final Icon DATASET_FILTER_GLOBAL =    load("/img/v0/filter/DatasetFilterGlobal.png");
    public static final Icon DATASET_FILTER_GLOBAL_ERR =    load("/img/v0/filter/DatasetFilterGlobalErr.png");
    public static final Icon DATASET_FILTER_EMPTY =    load("/img/v0/filter/DatasetFilterEmpty.png");

    public static final Icon DATASET_FILTER_CONDITION_ACTIVE =    load("/img/v0/ActiveFilterCondition.png");
    public static final Icon DATASET_FILTER_CONDITION_INACTIVE =    load("/img/v0/InactiveFilterCondition.png");
    public static final Icon DATASET_FILTER_CONDITION_REMOVE =    load("/img/v0/RemoveFilterCondition.png");
    public static final Icon DATASET_FILTER_CONDITION_NEW =    load("/img/v0/NewFilterCondition.png");


    public static final Icon CONDITION_JOIN_TYPE =    load("/img/v0/JoinTypeSwitch.png");

    public static final Icon TEXT_CELL_EDIT_ACCEPT = load("/img/v0/CellEditAccept.png");
    public static final Icon TEXT_CELL_EDIT_REVERT = load("/img/v0/CellEditRevert.png");
    public static final Icon TEXT_CELL_EDIT_DELETE = load("/img/v0/CellEditDelete.png");

    public static final Icon CALENDAR_CELL_EDIT_NEXT_MONTH = load("/img/v0/CalendarNextMonth.png");
    public static final Icon CALENDAR_CELL_EDIT_NEXT_YEAR = load("/img/v0/CalendarNextYear.png");
    public static final Icon CALENDAR_CELL_EDIT_PREVIOUS_MONTH = load("/img/v0/CalendarPreviousMonth.png");
    public static final Icon CALENDAR_CELL_EDIT_PREVIOUS_YEAR = load("/img/v0/CalendarPreviousYear.png");
    public static final Icon CALENDAR_CELL_EDIT_CLEAR_TIME = load("/img/v0/CalendarResetTime.png");

    public static final Icon EXEC_MESSAGES_INFO    = load("/img/v0/common/Info.png");
    public static final Icon EXEC_MESSAGES_WARNING = load("/img/v0/common/Warning.png");
    public static final Icon EXEC_MESSAGES_ERROR   = load("/img/v0/common/Error.png");

    public static final Icon CHECK   = load("/img/v0/common/Checked.png");
    public static final Icon PROJECT = load("/img/v0/project/Project.png");
    public static final Icon FILE_CONNECTION_MAPPING = load("/img/v0/FileConnection.png");
    public static final Icon FILE_SCHEMA_MAPPING = load("/img/v0/FileSchema.png");

    public static final Icon CODE_EDITOR_SAVE = load("/img/v0/action/SaveToDatabase.png");
    public static final Icon CODE_EDITOR_RESET = load("/img/v0/action/Reset.png");
    public static final Icon CODE_EDITOR_RELOAD = load("/img/v0/action/Refresh.png");
    public static final Icon CODE_EDITOR_DIFF = load("/img/v0/action/ShowDiff.png");
    public static final Icon CODE_EDITOR_DIFF_DB = load("/img/v0/action/ShowDbDiff.png");
    public static final Icon CODE_EDITOR_DDL_FILE = load("/img/v0/DDLFile.png");
    public static final Icon CODE_EDITOR_DDL_FILE_NEW = load("/img/v0/DDLFileNew.png");
    public static final Icon CODE_EDITOR_DDL_FILE_DETACH = load("/img/v0/DDLFileUnbind.png");
    public static final Icon CODE_EDITOR_DDL_FILE_ATTACH = load("/img/v0/DDLFileBind.png");
    public static final Icon CODE_EDITOR_SPEC = load("/img/v0/CodeSpec.png");
    public static final Icon CODE_EDITOR_BODY = load("/img/v0/CodeBody.png");

    public static final Icon OBEJCT_COMPILE     = load("/img/v0/action/Compile.png");
    public static final Icon OBEJCT_COMPILE_DEBUG = load("/img/v0/action/CompileDebug.png");
    //public static final Icon OBEJCT_COMPILE_KEEP = load("/img/CompileKeep.png");
    public static final Icon OBEJCT_COMPILE_ASK = load("/img/v0/action/CompileAsk.png");
    public static final Icon OBEJCT_EDIT_SOURCE = load("/img/v0/EditSource.png");
    public static final Icon OBEJCT_EDIT_DATA = load("/img/v0/EditData.png");
    public static final Icon OBEJCT_VIEW_DATA = load("/img/v0/ViewData.png");

    public static final Icon CONNECTION_COMMIT   = load("CONNECTION_COMMIT", "/img/v0/action/ConnectionCommit.png");
    public static final Icon CONNECTION_ROLLBACK = load("CONNECTION_ROLLBACK", "/img/v0/action/ConnectionRollback.png");

    public static final Icon COMMON_DIRECTION_IN = load("/img/v0/common/DirectionIn.png");
    public static final Icon COMMON_DIRECTION_OUT = load("/img/v0/common/DirectionOut.png");
    public static final Icon COMMON_DIRECTION_IN_OUT = load("/img/v0/common/DirectionInOut.png");




    public static final Icon CONN_STATUS_INVALID      = load("/img/v0/common/ErrorBig.png");
    public static final Icon CONN_STATUS_CONNECTED    = load("/img/v0/common/BulbOn.png");
    public static final Icon CONN_STATUS_DISCONNECTED = load("/img/v0/common/BulbOff.png");

    public static final Icon CONNECTION_VIRTUAL       = load("/img/v0/connection/ConnectionVirtual.png");
    public static final Icon CONNECTION_ACTIVE        = load("/img/v0/connection/ConnectionActive.png");
    public static final Icon CONNECTION_INACTIVE      = load("/img/v0/connection/ConnectionInactive.png");
    public static final Icon CONNECTION_DISABLED      = load("/img/v0/connection/ConnectionDisabled.png");
    public static final Icon CONNECTION_NEW           = load("/img/v0/connection/ConnectionNew.png");
    public static final Icon CONNECTION_INVALID       = load("/img/v0/connection/ConnectionInvalid.png");
    public static final Icon CONNECTIONS              = load("/img/v0/connection/Connections.png");


//    public static final Icon DBO_ARGUMENT_IN         = createRowIcon(DBO_ARGUMENT, COMMON_DIRECTION_IN);
//    public static final Icon DBO_ARGUMENT_OUT        = createRowIcon(DBO_ARGUMENT, COMMON_DIRECTION_OUT);
//    public static final Icon DBO_ARGUMENT_IN_OUT     = createRowIcon(DBO_ARGUMENT, COMMON_DIRECTION_IN_OUT);

    public static final Icon DBO_ATTRIBUTE           = load("/img/v0/object/Attribute.png");
    public static final Icon DBO_ATTRIBUTES          = load("/img/v0/object/Attributes.png");
    public static final Icon DBO_ARGUMENT            = load("/img/v0/object/Argument.png");
    public static final Icon DBO_ARGUMENTS           = load("/img/v0/object/Arguments.png");
    public static final Icon DBO_ARGUMENT_IN         = load("/img/v0/object/ArgumentIn.png");
    public static final Icon DBO_ARGUMENT_OUT        = load("/img/v0/object/ArgumentOut.png");
    public static final Icon DBO_ARGUMENT_IN_OUT     = load("/img/v0/object/ArgumentInOut.png");
    public static final Icon DBO_CLUSTER                = load("/img/v0/object/Cluster.png");
    public static final Icon DBO_CLUSTERS               = load("/img/v0/object/Clusters.png");
    public static final Icon DBO_COLUMN                 = load("/img/v0/object/Column.png");
    public static final Icon DBO_COLUMN_PK              = load("/img/v0/object/ColumnPk.png");
    public static final Icon DBO_COLUMN_FK              = load("/img/v0/object/ColumnFk.png");
    public static final Icon DBO_COLUMN_PFK             = load("/img/v0/object/ColumnPkFk.png");
    public static final Icon DBO_COLUMN_HIDDEN          = load("/img/v0/object/ColumnHidden.png");
    public static final Icon DBO_COLUMNS                = load("/img/v0/object/Columns.png");
    public static final Icon DBO_CONSTRAINT             = load("/img/v0/object/Constraint.png");
    public static final Icon DBO_CONSTRAINT_DISABLED    = load("/img/v0/object/ConstraintDisabled.png");
    public static final Icon DBO_CONSTRAINTS            = load("/img/v0/object/Constraints.png");
    public static final Icon DBO_DATABASE_LINK          = load("/img/v0/object/DatabaseLink.png");
    public static final Icon DBO_DATABASE_LINKS         = load("/img/v0/object/DatabaseLinks.png");
    public static final Icon DBO_DIMENSION              = load("/img/v0/object/Dimension.png");
    public static final Icon DBO_DIMENSIONS             = load("/img/v0/object/Dimensions.png");
    public static final Icon DBO_FUNCTION               = load("/img/v0/object/Function.png");
    public static final Icon DBO_FUNCTION_DEBUG         = load("/img/v0/object/FunctionDebug.png");
    public static final Icon DBO_FUNCTION_ERR           = load("/img/v0/object/FunctionErr.png");
    public static final Icon DBO_FUNCTIONS              = load("/img/v0/object/Functions.png");
    public static final Icon DBO_INDEX                  = load("/img/v0/object/Index.png");
    public static final Icon DBO_INDEX_DISABLED         = load("/img/v0/object/IndexDisabled.png");
    public static final Icon DBO_INDEXES                = load("/img/v0/object/Indexes.png");
    public static final Icon DBO_MATERIALIZED_VIEW      = load("/img/v0/object/MaterializedView.png");
    public static final Icon DBO_MATERIALIZED_VIEWS     = load("/img/v0/object/MaterializedViews.png");
    public static final Icon DBO_METHOD                 = load("/img/v0/object/Method.png");
    public static final Icon DBO_METHODS                = load("/img/v0/object/Methods.png");
    public static final Icon DBO_NESTED_TABLE           = load("/img/v0/object/NestedTable.png");
    public static final Icon DBO_NESTED_TABLES          = load("/img/v0/object/NestedTables.png");
    public static final Icon DBO_PACKAGE                = load("/img/v0/object/Package.png");
    public static final Icon DBO_PACKAGE_ERR            = load("/img/v0/object/PackageErr.png");
    public static final Icon DBO_PACKAGE_DEBUG          = load("/img/v0/object/PackageDebug.png");
    public static final Icon DBO_PACKAGES               = load("/img/v0/object/Packages.png");
    public static final Icon DBO_PACKAGE_SPEC           = load("DBO_PACKAGE_SPEC", "/img/v0/object/PackageSpec.png");
    public static final Icon DBO_PACKAGE_BODY           = load("DBO_PACKAGE_BODY", "/img/v0/object/PackageBody.png");
    public static final Icon DBO_PROCEDURE              = load("/img/v0/object/Procedure.png");
    public static final Icon DBO_PROCEDURE_ERR          = load("/img/v0/object/ProcedureErr.png");
    public static final Icon DBO_PROCEDURE_DEBUG        = load("/img/v0/object/ProcedureDebug.png");
    public static final Icon DBO_PROCEDURES             = load("/img/v0/object/Procedures.png");
    public static final Icon DBO_PRIVILEGE              = load("/img/v0/object/Privilege.png");
    public static final Icon DBO_PRIVILEGES             = load("/img/v0/object/Privileges.png");
    public static final Icon DBO_ROLE                   = load("/img/v0/object/Role.png");
    public static final Icon DBO_ROLES                  = load("/img/v0/object/Roles.png");
    public static final Icon DBO_SCHEMA                 = load("/img/v0/object/Schema.png");
    public static final Icon DBO_SCHEMAS                = load("/img/v0/object/Schemas.png");
    public static final Icon DBO_SYNONYM                = load("/img/v0/object/Synonym.png");
    public static final Icon DBO_SYNONYM_ERR            = load("/img/v0/object/SynonymErr.png");
    public static final Icon DBO_SYNONYMS               = load("/img/v0/object/Synonyms.png");
    public static final Icon DBO_SEQUENCE               = load("/img/v0/object/Sequence.png");
    public static final Icon DBO_SEQUENCES              = load("/img/v0/object/Sequences.png");
    public static final Icon DBO_TMP_TABLE              = load("/img/v0/object/TableTmp.png");
    public static final Icon DBO_TABLE                  = load("/img/v0/object/Table.png");
    public static final Icon DBO_TABLES                 = load("/img/v0/object/Tables.png");
    public static final Icon DBO_TRIGGER                = load("/img/v0/object/Trigger.png");
    public static final Icon DBO_TRIGGER_ERR            = load("/img/v0/object/TriggerErr.png");
    public static final Icon DBO_TRIGGER_DEBUG          = load("/img/v0/object/TriggerDebug.png");
    public static final Icon DBO_TRIGGER_ERR_DISABLED   = load("/img/v0/object/TriggerErrDisabled.png");
    public static final Icon DBO_TRIGGER_DISABLED       = load("/img/v0/object/TriggerDisabled.png");
    public static final Icon DBO_TRIGGER_DISABLED_DEBUG = load("/img/v0/object/TriggerDisabledDebug.png");
    public static final Icon DBO_TRIGGERS               = load("/img/v0/object/Triggers.png");
    public static final Icon DBO_TYPE                   = load("/img/v0/object/Type.png");
    public static final Icon DBO_TYPE_COLLECTION        = load("/img/v0/object/TypeCollection.png");
    public static final Icon DBO_TYPE_COLLECTION_ERR    = load("/img/v0/object/TypeCollectionErr.png");
    public static final Icon DBO_TYPE_ERR               = load("/img/v0/object/TypeErr.png");
    public static final Icon DBO_TYPE_DEBUG             = load("/img/v0/object/TypeDebug.png");
    public static final Icon DBO_TYPES                  = load("/img/v0/object/Types.png");
    public static final Icon DBO_USER                   = load("/img/v0/object/User.png");
    public static final Icon DBO_USER_EXPIRED           = load("/img/v0/object/UserExpired.png");
    public static final Icon DBO_USER_LOCKED            = load("/img/v0/object/UserLocked.png");
    public static final Icon DBO_USER_EXPIRED_LOCKED    = load("/img/v0/object/UserExpiredLocked.png");
    public static final Icon DBO_USERS                  = load("/img/v0/object/Users.png");
    public static final Icon DBO_VIEW                   = load("/img/v0/object/View.png");
    public static final Icon DBO_VIEW_SYNONYM           = load("/img/v0/object/ViewSynonym.png");
    public static final Icon DBO_VIEWS                  = load("/img/v0/object/Views.png");
    public static final Icon DBO_VARIABLE               = load("/img/v0/object/Variable.png");


    public static final Icon DEBUG_INVALID_BREAKPOINT  = load("/img/v0/InvalidBreakpoint.png");



    public static final Icon SPACE                        = load("/img/v0/Space.png");
    public static final Icon TREE_BRANCH                  = load("/img/v0/TreeBranch.png");
    public static final Icon SMALL_TREE_BRANCH            = load("/img/v0/SmallTreeBranch.png");






    private static Icon load(String path) {
        if (ApplicationInfo.getInstance().getBuild().getBaselineVersion() > 122) {
            path = path.replace("/img/v0/", "/img/v1/");
        }
        return IconLoader.getIcon(path);
    }

    private static Icon load(String key, String path) {
        Icon icon = load(path);
        REGISTERED_ICONS.put(key, icon);
        return icon;
    }

    public static Icon getIcon(String key) {
        return REGISTERED_ICONS.get(key);
    }

    private static Icon createRowIcon(Icon left, Icon right) {
        RowIcon rowIcon = new RowIcon(2);
        rowIcon.setIcon(left, 0);
        rowIcon.setIcon(right, 1);
        return rowIcon;
    }

}
