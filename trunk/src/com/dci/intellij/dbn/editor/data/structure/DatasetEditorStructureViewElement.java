package com.dci.intellij.dbn.editor.data.structure;

import com.dci.intellij.dbn.browser.model.BrowserTreeElement;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModel;
import com.dci.intellij.dbn.object.DBColumn;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.Arrays;
import java.util.List;

public class DatasetEditorStructureViewElement implements StructureViewTreeElement, Comparable{
    private BrowserTreeElement treeElement;
    private DatasetEditor datasetEditor;
    private StructureViewTreeElement[] children;

    DatasetEditorStructureViewElement(BrowserTreeElement treeElement, DatasetEditor datasetEditor) {
        this.treeElement = treeElement;
        this.datasetEditor = datasetEditor;
    }

    public BrowserTreeElement getValue() {
        return treeElement;
    }

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return treeElement.getPresentableText();
            }

            @Nullable
            public String getLocationString() {
                if (treeElement instanceof DBColumn) {
                    DBColumn column  = (DBColumn) treeElement;
                    return column.getPresentableTextDetails();

                }
                return null;
            }

            @Nullable
            public Icon getIcon(boolean open) {
                return treeElement.getIcon(0);
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }
        };
    }

    public StructureViewTreeElement[] getChildren() {
        if (children == null) {
            if (datasetEditor.isDisposed())  {
                return EMPTY_ARRAY;
            }
            DBDataset dataset = datasetEditor.getDataset();
            if (treeElement instanceof DBObjectBundle) {
                DatasetEditorStructureViewElement schemaStructureElement =
                        new DatasetEditorStructureViewElement(dataset.getSchema(), datasetEditor);
                children = new StructureViewTreeElement[] {schemaStructureElement};
            }
            else if (treeElement instanceof DBSchema) {
                DatasetEditorStructureViewElement datasetStructureElement =
                        new DatasetEditorStructureViewElement(dataset, datasetEditor);
                children = new StructureViewTreeElement[] {datasetStructureElement};
            }
            else if (treeElement instanceof DBDataset) {
                List<DBColumn> columns = dataset.getColumns();
                children = new StructureViewTreeElement[columns.size()];
                for (int i=0; i<children.length; i++) {
                    children[i] = new DatasetEditorStructureViewElement(columns.get(i), datasetEditor);
                }
                Arrays.sort(children);
            }
            else {
                children = EMPTY_ARRAY;
            }
        }
        return children;
    }

    public void navigate(boolean requestFocus) {
        if (!datasetEditor.isDisposed()) {
            DatasetEditorTable table = datasetEditor.getEditorTable();
            table.cancelEditing();
            DatasetEditorModel model = table.getModel();
            if (treeElement instanceof DBColumn &&  model.getSize() > 0) {
                DBColumn column = (DBColumn) treeElement;
                int modelColumnIndex = model.getHeader().indexOfColumn(column);
                int tableColumnIndex = table.convertColumnIndexToView(modelColumnIndex);
                int rowIndex = table.getSelectedRow();
                if (rowIndex == -1)  rowIndex = 0;
                table.selectCell(rowIndex, tableColumnIndex);
                if (requestFocus) {
                    table.requestFocus();
                }
            }
        }
    }

    public boolean canNavigate() {
        return !datasetEditor.isDisposed();
    }

    public boolean canNavigateToSource() {
        return treeElement instanceof DBColumn && !datasetEditor.isDisposed() && datasetEditor.getEditorTable().getRowCount() > 0;
    }

    public int compareTo(Object o) {
        DatasetEditorStructureViewElement desve = (DatasetEditorStructureViewElement) o;
        if (treeElement instanceof DBColumn && desve.treeElement instanceof DBColumn) {
            DBColumn thisColumn = (DBColumn) treeElement;
            DBColumn remoteColumn = (DBColumn) desve.treeElement;
            return thisColumn.compareTo(remoteColumn);
        }
        return 0;
    }
}
