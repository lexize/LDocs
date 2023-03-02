package org.lexize.ldocs.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LDocsScrollWidget extends AbstractContainerWidget {
    private final List<AbstractWidget> children = new ArrayList<>(1);
    private double scroll;
    public LDocsScrollWidget(int i, int j, int k, int l) {
        super(i, j, k, l, Component.empty());
    }

    public AbstractWidget getContainedWidget() {
        return children.size() > 0 ? children.get(0) : null;
    }

    public void setContainedWidget(AbstractWidget containedWidget) {
        boolean added = false;
        if (children.size() < 1) {
            if (containedWidget != null) {
                children.add(containedWidget);
                added = true;
            }
        }
        else {
            if (containedWidget == null) children.remove(0);
            else {
                children.set(0, containedWidget);
                added = true;
            }
        }
        if (added) containedWidget.setWidth(width);
    }

    public double getScroll() {
        return scroll;
    }

    public void setScroll(double scroll) {
        this.scroll = scroll;
    }

    @Override
    protected List<? extends AbstractWidget> getContainedChildren() {
        return children;
    }
    private int sizeDiff() {
        if (getContainedWidget() == null) return 0;
        return Math.max(0, getContainedWidget().getHeight() - height);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX < getX() + getWidth() &&
                mouseY >= getY() && mouseY < getY() + getHeight();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) return true;
        else if (isMouseOver(mouseX, mouseY)) {
            int sz = sizeDiff();
            scroll = Math.min(sz, Math.max(0, scroll - (amount*5)));
            return true;
        }
        return false;
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        while (children.size() > 1) children.remove(1);
        var widget = getContainedWidget();
        if (widget != null) {
            widget.setX(getX());
            widget.setY(getY()-((int)scroll));
        }
        super.renderButton(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        return super.getChildAt(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void setWidth(int value) {
        super.setWidth(value);
        var widget = getContainedWidget();
        if (widget != null) {
            widget.setWidth(value);
        }
    }
}
