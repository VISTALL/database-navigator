package com.dci.intellij.dbn.common.ui;

import com.intellij.openapi.ui.Splitter;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class UIUtil {
    public static final Font REGULAR_FONT = com.intellij.util.ui.UIUtil.getLabelFont();
    public static final Font BOLD_FONT = new Font(REGULAR_FONT.getName(), Font.BOLD, REGULAR_FONT.getSize());
    public static final String DARK_LAF_NAME = "Darcula";

    public static void updateSplitterProportion(final JComponent root, final float proportion) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (root instanceof Splitter) {
                    Splitter splitter = (Splitter) root;
                    splitter.setProportion(proportion);
                } else {
                    Component[] components = root.getComponents();
                    for (Component component : components) {
                        if (component instanceof JComponent) {
                            updateSplitterProportion((JComponent) component, proportion);
                        }
                    }
                }
            }
        });

    }
    
    public static Point getRelativeMouseLocation(Component component) {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo == null) {
            return new Point();
        } else {
            Point mouseLocation = pointerInfo.getLocation();
            return getRelativeLocation(mouseLocation, component);
        }
    }
    
    public static Point getRelativeLocation(Point locationOnScreen, Component component) {
        Point componentLocation = component.getLocationOnScreen();
        Point relativeLocation = locationOnScreen.getLocation();
        relativeLocation.move(
                (int) (locationOnScreen.getX() - componentLocation.getX()), 
                (int) (locationOnScreen.getY() - componentLocation.getY()));
        return relativeLocation;
    }

    public static boolean isChildOf(Component component, Component child) {
        Component parent = child == null ? null : child.getParent();
        while (parent != null) {
            if (parent == component) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public static boolean isDarkLookAndFeel() {
        return UIManager.getLookAndFeel().getName().contains(DARK_LAF_NAME);
    }

    public static boolean supportsDarkLookAndFeel() {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            if (lookAndFeelInfo.getName().contains(DARK_LAF_NAME)) return true;
        }
        return false;
    }

}
