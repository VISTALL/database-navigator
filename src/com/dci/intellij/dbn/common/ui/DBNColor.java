package com.dci.intellij.dbn.common.ui;

import java.awt.Color;

public class DBNColor extends Color {
    public DBNColor(int rgb, int darkRGB) {
        super(isDark() ? darkRGB : rgb);
    }

    public DBNColor(Color regular, Color dark) {
        super(isDark() ? dark.getRGB() : regular.getRGB(), (isDark() ? dark : regular).getAlpha() != 255);
    }

    private static boolean isDark() {
        return UIUtil.isDarkLookAndFeel();
    }
}
