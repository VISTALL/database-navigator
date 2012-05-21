package com.dci.intellij.dbn.data.ui.table;

import com.dci.intellij.dbn.common.ui.table.SortableTable;
import com.dci.intellij.dbn.data.ui.table.listener.ResultSetTableMouseListener;
import com.dci.intellij.dbn.data.ui.table.model.ResultSetDataModel;
import com.dci.intellij.dbn.data.ui.table.record.RecordViewInfo;
import com.dci.intellij.dbn.data.ui.table.record.TableRecordViewerDialog;

public class ResultSetTable extends SortableTable {
    private RecordViewInfo recordViewInfo;
    public ResultSetTable(ResultSetDataModel dataModel, boolean enableSpeedSearch, RecordViewInfo recordViewInfo) {
        super(dataModel, enableSpeedSearch);
        this.recordViewInfo = recordViewInfo;
        addMouseListener(new ResultSetTableMouseListener(this));
    }

    public RecordViewInfo getRecordViewInfo() {
        return recordViewInfo;
    }

    @Override
    public ResultSetDataModel getModel() {
        return (ResultSetDataModel) super.getModel();
    }

    public void showRecordViewDialog() {
        TableRecordViewerDialog dialog = new TableRecordViewerDialog(this);
        dialog.show();
    }
}
