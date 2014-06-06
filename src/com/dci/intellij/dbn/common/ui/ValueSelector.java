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
    private Icon noSelectionIcon;
    private String noSelectionText;
    private boolean focused;

    public ValueSelector(Icon noSelectionIcon, String noSelectionText, T preselectedValue) {
        super(new BorderLayout(0,0));
        this.noSelectionIcon = noSelectionIcon;
        this.noSelectionText = noSelectionText;
        label = new JLabel();
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(labelMouseListener);
        add(label, BorderLayout.WEST);
        selectValue(preselectedValue);
        label.addMouseListener(mouseListener);
        setBorder(new EmptyBorder(1,6,1,6));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            setBorder(new CompoundBorder(new RoundedLineBorder(new JBColor(Gray._190, Gray._55), 5), new EmptyBorder(0, 5, 0, 5)));
            setBackground(new JBColor(Gray._210, Gray._75));
            updateUI();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBorder(new EmptyBorder(1, 6, 1, 6));
            setBackground(UIUtil.getPanelBackground());
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
                (int) locationOnScreen.getY() + getHeight());
        popup.showInScreenCoordinates(this, location);
    }

    public T getSelectedValue() {
        return selectedValue;
    }

    public abstract List<T> getAllValues();
    public abstract void valueSelected(T value);

    private void selectValue(T value) {
        selectedValue = value;
        if (selectedValue == null) {
            label.setIcon(noSelectionIcon);
            label.setText(noSelectionText);
        } else {
            label.setIcon(selectedValue.getIcon());
            label.setText(selectedValue.getName());
        }
        valueSelected(selectedValue);
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
