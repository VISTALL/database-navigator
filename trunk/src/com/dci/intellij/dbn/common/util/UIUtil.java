package com.dci.intellij.dbn.common.util;

import com.intellij.openapi.ui.Splitter;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;

public class UIUtil {
    public static final Font REGULAR_FONT = com.intellij.util.ui.UIUtil.getLabelFont();
    public static final Font BOLD_FONT = new Font(REGULAR_FONT.getName(), Font.BOLD, REGULAR_FONT.getSize());

    public static void updateSplitterProportion(JComponent root, float proportion) {
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
    
    public static Point getRelativeMouseLocation(Component component) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        return getRelativeLocation(mouseLocation, component);
    }
    
    public static Point getRelativeLocation(Point locationOnScreen, Component component) {
        Point componentLocation = component.getLocationOnScreen();
        Point relativeLocation = locationOnScreen.getLocation();
        relativeLocation.move(
                (int) (locationOnScreen.getX() - componentLocation.getX()), 
                (int) (locationOnScreen.getY() - componentLocation.getY()));
        return relativeLocation;
    }
}
