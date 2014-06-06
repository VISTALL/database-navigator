package com.dci.intellij.dbn.common.ui.list;

import com.intellij.util.ui.UIUtil;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CheckBoxList<T extends Selectable> extends JList {
    private boolean mutable;
    public CheckBoxList(List<T> elements) {
        this(elements, false);
    }
    public CheckBoxList(List<T> elements, boolean mutable) {
        this.mutable = mutable;
        setSelectionMode(mutable ?
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION :
                ListSelectionModel.SINGLE_SELECTION);

        setCellRenderer(new CellRenderer());
        DefaultListModel model = new DefaultListModel();
        for (T element : elements) {
            model.addElement(new Entry<T>(element));
        }
        setModel(model);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (isEnabled() && e.getButton() == MouseEvent.BUTTON1) {
                    int index = locationToIndex(e.getPoint());

                    if (index != -1) {
                        Entry entry = (Entry) getModel().getElementAt(index);
                        if ((e.getClickCount() == 1 && e.getX() < 20) || e.getClickCount() == 2) {
                            entry.switchSelection();
                        }
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    int[] indices = CheckBoxList.this.getSelectedIndices();
                    for (int index : indices) {
                        if (index >= 0) {
                            Entry entry = (Entry) getModel().getElementAt(index);
                            entry.switchSelection();
                        }
                    }
                }
            }
        });
    }

    public boolean isSelected(T presentable) {
        for (int i=0; i<getModel().getSize(); i++) {
            Entry<T> entry = (Entry<T>) getModel().getElementAt(i);
            if (entry.getPresentable().equals(presentable)) {
                return entry.isSelected();
            }
        }
        return false;
    }

    private class CellRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Entry entry = (Entry) value;
            Selectable presentable = entry.presentable;
            entry.checkBox.setEnabled(presentable.isMasterSelected());

            //entry.errorLabel.setText(error != null && list.isEnabled() ? " - " + error : "");

            if (mutable) {
                Color foreground = isSelected ? UIUtil.getListSelectionForeground() : entry.isSelected() ? UIUtil.getListForeground() : UIUtil.getMenuItemDisabledForeground();
                Color background = isSelected ? UIUtil.getListSelectionBackground() : UIUtil.getListBackground();
                entry.textPanel.setBackground(background);
                entry.checkBox.setBackground(background);
                entry.label.setForeground(foreground);
            } else {
                Color background = list.isEnabled() ? UIUtil.getListBackground() : UIUtil.getComboBoxDisabledBackground();
                //entry.setBackground(background);
                entry.textPanel.setBackground(background);
                entry.checkBox.setBackground(background);
                entry.setBorder(new LineBorder(background));
                entry.label.setForeground(presentable.isMasterSelected() ? UIUtil.getListForeground() : UIUtil.getMenuItemDisabledForeground());
            }

            return entry;
        }
    }

    public void sortElements(final Comparator<T> comparator) {
        List<Entry<T>> entries = new ArrayList<Entry<T>>();
        ListModel model = getModel();
        for (int i=0; i<model.getSize(); i++) {
            Entry<T> entry = (Entry<T>) model.getElementAt(i);
            entries.add(entry);
        }
        if (comparator == null)
            Collections.sort(entries); else
            Collections.sort(entries, new Comparator<Entry<T>>() {
                @Override
                public int compare(Entry<T> o1, Entry<T> o2) {
                    return comparator.compare(o1.presentable, o2.presentable);
                }
            });
        DefaultListModel newModel = new DefaultListModel();
        for (Entry<T> entry : entries) {
            newModel.addElement(entry);
        }
        setModel(newModel);
    }

    public void applyChanges(){
        ListModel model = getModel();
        for (int i=0; i<model.getSize(); i++) {
            Entry entry = (Entry) model.getElementAt(i);
            entry.updatePresentable();
        }

    }

    public void addActionListener(ActionListener actionListener) {
        DefaultListModel model = (DefaultListModel) getModel();
        for (Object o : model.toArray()) {
            Entry entry = (Entry) o;
            entry.checkBox.addActionListener(actionListener);
        }
    }

    public void removeActionListener(ActionListener actionListener) {
        DefaultListModel model = (DefaultListModel) getModel();
        for (Object o : model.toArray()) {
            Entry entry = (Entry) o;
            entry.checkBox.removeActionListener(actionListener);
        }
    }


    private class Entry<T extends Selectable> extends JPanel implements Comparable<Entry<T>> {
        private JPanel textPanel;
        private JCheckBox checkBox;
        private JLabel label;
        private JLabel errorLabel;
        private T presentable;

        private Entry(T presentable) {
            super(new BorderLayout(4, 0));
            setBackground(UIUtil.getListBackground());
            this.presentable = presentable;
            checkBox = new JCheckBox("", presentable.isSelected());
            checkBox.setBackground(UIUtil.getListBackground());

            label = new JLabel(presentable.getName(), presentable.getIcon(), SwingConstants.LEFT);
            //label.setForeground(error != null ? Color.RED : GUIUtil.getListForeground());
            errorLabel = new JLabel();
            errorLabel.setForeground(Color.RED);
            add(checkBox, BorderLayout.WEST);

            textPanel = new JPanel(new BorderLayout(4, 0));
            textPanel.add(label, BorderLayout.WEST);
            textPanel.add(errorLabel, BorderLayout.CENTER);
            textPanel.setBackground(UIUtil.getListBackground());
            add(textPanel, BorderLayout.CENTER);
        }

        private void updatePresentable() {
            presentable.setSelected(checkBox.isSelected());
        }

        public T getPresentable() {
            return presentable;
        }

        public boolean isSelected() {
            return checkBox.isSelected();
        }

        private void switchSelection() {
            //if (checkBox.isEnabled()){
                checkBox.setSelected(!checkBox.isSelected());
                CheckBoxList.this.repaint();
                for (ActionListener actionListener : checkBox.getActionListeners()) {
                    actionListener.actionPerformed(new ActionEvent(checkBox, 0, "selectionChanged"));
                }
            //}
        }

        @Override
        public int compareTo(Entry<T> o) {
            return presentable.compareTo(o.presentable);
        }
    }
}
