package com.dci.intellij.dbn.common.ui.table.renderer;

import com.dci.intellij.dbn.common.Colors;
import com.dci.intellij.dbn.common.ui.table.BasicTable;
import com.dci.intellij.dbn.common.ui.table.SortableTable;
import com.dci.intellij.dbn.common.ui.table.model.BasicDataModel;
import com.dci.intellij.dbn.common.ui.table.model.DataModel;
import com.dci.intellij.dbn.common.ui.table.model.DataModelCell;
import com.dci.intellij.dbn.common.util.TextAttributesUtil;
import com.dci.intellij.dbn.data.find.DataSearchResult;
import com.dci.intellij.dbn.data.find.DataSearchResultMatch;
import com.dci.intellij.dbn.data.value.LazyLoadedValue;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTableCellRenderer;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.JTable;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.util.Iterator;


public class BasicTableCellRenderer extends ColoredTableCellRenderer {
    private Color ROW_HIGHLIGHT = EditorColorsManager.getInstance().getGlobalScheme().getColor(ColorKey.find("CARET_ROW_COLOR"));

    public BasicTableCellRenderer(Project project) {
    }

    protected void customizeCellRenderer(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {
        SortableTable sortableTable = (SortableTable) table;
        DataModelCell cell = (DataModelCell) value;
        if (cell != null && cell.getUserValue() != null) {
            //append(cell.getFormattedUserValue(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            SimpleTextAttributes textAttributes = cell.getUserValue() instanceof LazyLoadedValue ?
                    SimpleTextAttributes.GRAYED_ATTRIBUTES :
                    SimpleTextAttributes.REGULAR_ATTRIBUTES;
            writeUserValue(cell, textAttributes);
        }

        //updateBorder(cell, sortableTable);
        if (table.getSelectedRow() == row && !match(table.getSelectedColumns(), column) && table.getCellSelectionEnabled()) {
            setBackground(ROW_HIGHLIGHT);
        } else if (sortableTable.isLoading() && !selected) {
            setBackground(Colors.DSE_CELL_BACKGROUND_DISABLED);
        }
    }

    protected void updateBorder(DataModelCell cell, BasicTable table) {
        BasicDataModel model = table.getModel();
        if (model.hasSearchResult()) {
            DataSearchResult searchResult = model.getSearchResult();
            if (searchResult.getSelectedMatch() != null && searchResult.getSelectedMatch().getCell() == cell) {
                setBorder(new LineBorder(Color.BLACK));
            }
        } else {
            setBorder(null);
        }
    }

    protected void writeUserValue(DataModelCell cell, SimpleTextAttributes textAttributes) {
         String formattedUserValue;
         if (cell.getUserValue() instanceof String) {
             formattedUserValue = (String) cell.getUserValue();
             if (formattedUserValue.indexOf('\n') > -1) {
                 formattedUserValue = formattedUserValue.replace('\n', ' ');
             }

         } else {
             formattedUserValue = cell.getFormattedUserValue();
         }

         DataModel model = cell.getRow().getModel();
         if (model.hasSearchResult()) {
             DataSearchResult searchResult = model.getSearchResult();

             Iterator<DataSearchResultMatch> matches = searchResult.getMatches(cell);
             if (matches != null) {
                 int lastEndOffset = 0;
                 SimpleTextAttributes searchResultAttributes = TextAttributesUtil.getSimpleTextAttributes(EditorColors.TEXT_SEARCH_RESULT_ATTRIBUTES);
                 DataSearchResultMatch selectedMatch = searchResult.getSelectedMatch();
                 if (selectedMatch != null && selectedMatch.getCell() == cell) {
                    searchResultAttributes = new SimpleTextAttributes(
                            TextAttributesUtil.getColor(EditorColors.SELECTION_BACKGROUND_COLOR),
                            TextAttributesUtil.getColor(EditorColors.SELECTION_FOREGROUND_COLOR),
                            null, SimpleTextAttributes.STYLE_PLAIN);
                 }
                 
                 while (matches.hasNext()) {
                     DataSearchResultMatch match = matches.next();
                     if (match.getStartOffset() > lastEndOffset) {
                         append(formattedUserValue.substring(lastEndOffset, match.getStartOffset()), textAttributes);
                     }
                     append(formattedUserValue.substring(match.getStartOffset(), match.getEndOffset()), searchResultAttributes);
                     lastEndOffset = match.getEndOffset();

                 }
                 if (lastEndOffset < formattedUserValue.length()) {
                     append(formattedUserValue.substring(lastEndOffset), textAttributes);
                 }
             } else {
                 append(formattedUserValue, textAttributes);
             }

         } else {
             append(formattedUserValue, textAttributes);
         }
     }

    boolean match(int[] indexes, int index) {
        for (int idx : indexes) {
            if (idx == index) return true;
        }
        return false;
    }
}
