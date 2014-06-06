package com.dci.intellij.dbn.common.ui;

import com.dci.intellij.dbn.common.util.NamingUtil;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.RoundedLineBorder;
import com.intellij.util.ui.UIUtil;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public abstract class ValueSelector<T extends Presentable> extends JPanel{
    private T selectedValue;
    private JLabel label;
    private JPanel innerPanel;
    private Icon icon;
    private String text;
    private boolean selectInternal;

    public ValueSelector(Icon icon, String text, T preselectedValue, boolean selectInternal) {
        super(new BorderLayout(0,0));
        this.icon = icon;
        this.text = text;
        this.selectInternal = selectInternal;

        setBorder(new EmptyBorder(2, 0, 2, 0));

        label = new JLabel(text, icon, SwingConstants.LEFT);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(labelMouseListener);
        label.addMouseListener(mouseListener);

        innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBorder(new EmptyBorder(21 - icon.getIconHeight(), 6, 21 - icon.getIconHeight(), 6));
        innerPanel.add(label, BorderLayout.WEST);
        add(innerPanel, BorderLayout.CENTER);

        if (selectInternal) selectValue(preselectedValue);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            innerPanel.setBorder(new CompoundBorder(new RoundedLineBorder(new JBColor(Gray._190, Gray._55), 3), new EmptyBorder(20 - icon.getIconHeight(), 5, 20 - icon.getIconHeight(), 5)));
            innerPanel.setBackground(new JBColor(Gray._210, Gray._75));
            updateUI();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            innerPanel.setBorder(new EmptyBorder(21 - icon.getIconHeight(), 6, 21 - icon.getIconHeight(), 6));
            innerPanel.setBackground(UIUtil.getPanelBackground());
            updateUI();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }
    };

    private MouseListener labelMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            showPopup();
        }
    };

    private void showPopup() {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (T value : getAllValues()) {
            actionGroup.add(new SelectValueAction(value));
        }
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                null,
                actionGroup,
                DataManager.getInstance().getDataContext(this),
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true, null, 10);

        Point locationOnScreen = getLocationOnScreen();
        Point location = new Point(
                (int) (locationOnScreen.getX()),
                (int) locationOnScreen.getY() + getHeight() + 1);
        popup.showInScreenCoordinates(this, location);
    }

    public T getSelectedValue() {
        return selectedValue;
    }

    public abstract List<T> getAllValues();
    public abstract void valueSelected(T value);

    private void selectValue(T value) {
        if (selectInternal) {
            selectedValue = value;
            if (selectedValue == null) {
                label.setIcon(icon);
                label.setText(text);
            } else {
                label.setIcon(selectedValue.getIcon());
                label.setText(selectedValue.getName());
            }
        }
        valueSelected(value);
    }

    public class SelectValueAction extends DumbAwareAction {
        private T value;

        public SelectValueAction(T value) {
            super(NamingUtil.enhanceUnderscoresForDisplay(value.getName()), null, value.getIcon());
            this.value = value;
        }

        public void actionPerformed(AnActionEvent e) {
            selectValue(value);
        }

    }
}
