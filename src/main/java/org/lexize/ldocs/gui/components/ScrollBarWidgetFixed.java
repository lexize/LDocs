package org.lexize.ldocs.gui.components;

import org.moon.figura.gui.widgets.ScrollBarWidget;

public class ScrollBarWidgetFixed extends ScrollBarWidget {
    public ScrollBarWidgetFixed(int x, int y, int width, int height, double initialValue) {
        super(x, y, width, height, initialValue);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}
