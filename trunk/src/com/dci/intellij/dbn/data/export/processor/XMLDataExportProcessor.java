package com.dci.intellij.dbn.data.export.processor;

import com.dci.intellij.dbn.common.locale.Formatter;
import com.dci.intellij.dbn.common.locale.options.RegionalSettings;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.data.export.DataExportException;
import com.dci.intellij.dbn.data.export.DataExportFormat;
import com.dci.intellij.dbn.data.export.DataExportInstructions;
import com.dci.intellij.dbn.data.export.DataExportModel;
import com.dci.intellij.dbn.data.type.BasicDataType;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Date;


public class XMLDataExportProcessor extends DataExportProcessor{
    protected DataExportFormat getFormat() {
        return DataExportFormat.XML;
    }

    @Override
    public String getFileExtension() {
        return "xml";
    }

    @Override
    public String adjustFileName(String fileName) {
        if (!fileName.contains(".xml")) {
            fileName = fileName + ".xml";
        }
        return fileName;
    }

    public boolean canCreateHeader() {
        return false;
    }

    public boolean canExportToClipboard() {
        return true;
    }

    public boolean canQuoteValues() {
        return false;
    }

    @Override
    public Transferable createClipboardContent(String content) {
        return new XmlContent(content);
    }

    public class XmlContent implements Transferable {
        private DataFlavor[] dataFlavors;
        private String content;

        public XmlContent(String htmlText) {
            content = htmlText;
            try {
                dataFlavors = new DataFlavor[3];
                dataFlavors[0] = new DataFlavor("text/xml;class=java.lang.String");
                dataFlavors[1] = new DataFlavor("text/rtf;class=java.lang.String");
                dataFlavors[2] = new DataFlavor("text/plain;class=java.lang.String");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            return dataFlavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return
                    "text/xml".equals(flavor.getMimeType()) ||
                    "text/rtf".equals(flavor.getMimeType()) ||
                    "text/plain".equals(flavor.getMimeType());
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException{
            return content;
        }
    }


    public void performExport(DataExportModel model, DataExportInstructions instructions, ConnectionHandler connectionHandler) throws DataExportException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<table name=\"" + model.getTableName() + "\">\n");
        RegionalSettings regionalSettings = RegionalSettings.getInstance(connectionHandler.getProject());

        for (int rowIndex=0; rowIndex < model.getRowCount(); rowIndex++) {
            buffer.append("    <row index=\"" + rowIndex + "\">\n");
            for (int columnIndex=0; columnIndex < model.getColumnCount(); columnIndex++){
                String columnName = model.getColumnName(columnIndex);
                BasicDataType basicDataType = model.getBasicDataType(columnIndex);
                String value = null;
                if (basicDataType == BasicDataType.LITERAL ||
                        basicDataType == BasicDataType.NUMERIC ||
                        basicDataType == BasicDataType.DATE_TIME) {

                    Object object = model.getValue(rowIndex, columnIndex);

                    if (object != null) {
                        Formatter formatter = regionalSettings.getFormatter();
                        if (object instanceof Number) {
                            Number number = (Number) object;
                            value = formatter.formatNumber(number);
                        } else if (object instanceof Date) {
                            Date date = (Date) object;
                            value = hasTimeComponent(date) ?
                                    formatter.formatDateTime(date) :
                                    formatter.formatDate(date);
                        } else {
                            value = object.toString();
                        }
                    }
                }

                if (value == null) value = "";

                boolean isCDATA = StringUtil.containsOneOf(value, "\n", "<", ">");
                boolean isWrap = value.length() > 100 || isCDATA;

                buffer.append("        <column name=\"" + columnName + "\">");
                if (isWrap) {
                    value = ("\n" + value);//.replace("\n", "\n            ");
                }
                
                if (isCDATA) {
                    buffer.append("\n            <![CDATA[");
                    buffer.append(value);
                    buffer.append("\n            ]]>");
                } else {
                    buffer.append(value);
                }
                buffer.append(isWrap ? "\n        </column>\n" : "</column>\n");
            }

            buffer.append("    </row>\n");
        }
        buffer.append("</table>\n");


        writeContent(instructions, buffer.toString());
    }
}
