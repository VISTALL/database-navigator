package com.dci.intellij.dbn.common.ui;

import com.dci.intellij.dbn.common.Icons;
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
import javax.swing.border.Border;
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
    private boolean isComboBox;
    private boolean isShowingPopup = false;

    private Border focusBorder;
    private Border defaultBorder;
    private Border insideBorder;
    private Border outsideBorder = new EmptyBorder(2, 0, 2, 0);


    public ValueSelector(Icon icon, String text, T preselectedValue, boolean isComboBox) {
        super(new BorderLayout(0,0));
        this.icon = icon;
        this.text = text;
        this.isComboBox = isComboBox;


        setBorder(outsideBorder);

        insideBorder = isComboBox ?
                new EmptyBorder(21 - icon.getIconHeight(), 5, 21 - icon.getIconHeight(), 5) :
                new EmptyBorder(20 - icon.getIconHeight(), 6, 20 - icon.getIconHeight(), 6);

        defaultBorder = isComboBox ? new CompoundBorder(new RoundedLineBorder(new JBColor(Gray._210, Gray._75), 3), insideBorder) : insideBorder;
        focusBorder = new CompoundBorder(new RoundedLineBorder(new JBColor(Gray._190, Gray._55), 3), insideBorder);

        label = new JLabel(text, icon, SwingConstants.LEFT);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(mouseListener);

        innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBorder(defaultBorder);
        innerPanel.add(label, BorderLayout.WEST);
        innerPanel.addMouseListener(mouseListener);
        innerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(innerPanel, BorderLayout.CENTER);


        if (isComboBox) {
            selectValue(preselectedValue);
            innerPanel.setBackground(UIUtil.getTextFieldBackground());
            innerPanel.add(new JLabel(Icons.COMMON_ARROW_DOWN), BorderLayout.EAST);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isShowingPopup) {
                innerPanel.setBorder(focusBorder);
                innerPanel.setBackground(new JBColor(Gray._210, Gray._75));
                updateUI();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!isShowingPopup) {
                innerPanel.setBorder(defaultBorder);
                //innerPanel.setBorder(new EmptyBorder(21 - icon.getIconHeight(), 6, 21 - icon.getIconHeight(), 6));
                innerPanel.setBackground(isComboBox ? UIUtil.getTextFieldBackground() : UIUtil.getPanelBackground());
                updateUI();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!isShowingPopup) {
                showPopup();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }
    };


    private void showPopup() {
        isShowingPopup = true;
        innerPanel.setCursor(Cursor.getDefaultCursor());
        label.setCursor(Cursor.getDefaultCursor());
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (T value : getAllValues()) {
            actionGroup.add(new SelectValueAction(value));
        }
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                null,
                actionGroup,
                DataManager.getInstance().getDataContext(this),
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true, new Runnable() {
                    @Override
                    public void run() {
                        isShowingPopup = false;
                        innerPanel.setBorder(defaultBorder);
                        innerPanel.setBackground(isComboBox ? UIUtil.getTextFieldBackground() : UIUtil.getPanelBackground());
                        innerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        updateUI();
                    }
                }, 10);

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
        if (isComboBox) {
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
