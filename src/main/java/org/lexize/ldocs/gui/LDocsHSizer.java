package org.lexize.ldocs.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LDocsHSizer extends AbstractContainerWidget implements LDocsYResizableWidget {
    private static final Color SIZER_COLOR = new Color(1f,1f,1f,0.1f);
    private static final Color SIZER_HOVERED_COLOR = new Color(1f,1f,1f,0.75f);
    private final ArrayList<AbstractWidget> children = new ArrayList<>();
    private AbstractWidget leftWidget;
    private AbstractWidget rightWidget;
    private int sizerWidth = 3;
    private int sizerPadding = 3;
    private float sizerProgress = 0.5f;
    private float minSizerProgress = 0.1f;
    private float maxSizerProgress = 0.9f;
    private boolean changingSizerPos = false;
    public LDocsHSizer(int i, int j, int k, int l) {
        super(i, j, k, l, Component.empty());
    }

    private Minecraft getClient() {
        return Minecraft.getInstance();
    }

    @Override
    protected List<? extends AbstractWidget> getContainedChildren() {
        return children;
    }
    public void setLeftWidget(AbstractWidget widget) {
        if (leftWidget != null) children.remove(leftWidget);
        if (widget != null) children.add(widget);
        leftWidget = widget;
        update();
    }

    public void setRightWidget(AbstractWidget widget) {
        if (rightWidget != null) children.remove(rightWidget);
        if (widget != null) children.add(widget);
        rightWidget = widget;
        update();
    }

    public AbstractWidget getLeftWidget() {
        return leftWidget;
    }

    public AbstractWidget getRightWidget() {
        return rightWidget;
    }

    public int getSizerWidth() {
        return sizerWidth;
    }

    public void setSizerWidth(int sizerWidth) {
        this.sizerWidth = sizerWidth;
        update();
    }

    public int getSizerPadding() {
        return sizerPadding;
    }

    public void setSizerPadding(int sizerPadding) {
        this.sizerPadding = sizerPadding;
        update();
    }

    public float getSizerProgress() {
        return sizerProgress;
    }

    public void setSizerProgress(float sizerProgress) {
        this.sizerProgress = sizerProgress;
        update();
    }

    public float getMinSizerProgress() {
        return minSizerProgress;
    }

    public void setMinSizerProgress(float minSizerProgress) {
        this.minSizerProgress = minSizerProgress;
    }

    public float getMaxSizerProgress() {
        return maxSizerProgress;
    }

    public void setMaxSizerProgress(float maxSizerProgress) {
        this.maxSizerProgress = maxSizerProgress;
    }

    public void update() {
        int widgetWidth = getWidth();
        if (leftWidget != null) {
            leftWidget.setX(getX());
            leftWidget.setY(getY());
            int lWidth = (int)((widgetWidth * sizerProgress) - sizerPadding - (sizerWidth /2));
            leftWidget.setWidth(lWidth);
        }
        if (rightWidget != null) {
            int rXPos = (int)((widgetWidth * sizerProgress) + sizerPadding + (sizerWidth /2));
            rightWidget.setX(rXPos);
            rightWidget.setY(getY());
            rightWidget.setWidth(widgetWidth - rXPos);
        }
    }

    private boolean mouseInSizer(double mouseX, double mouseY) {
        int widgetWidth = getWidth();
        float sizerPos = (widgetWidth * sizerProgress);
        float xPos = sizerPos - (sizerWidth / 2f);
        return (mouseX >= xPos && mouseX <= xPos + sizerWidth) && (mouseY >= getY() && mouseY <= getY()+getHeight());
    }

    @Override
    public void setHeight(int value) {
        height = value;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        //int width, height;
        //width = getClient().getWindow().getScreenWidth();
        //height = getClient().getWindow().getScreenWidth();
        Color barColor = mouseInSizer(mouseX, mouseY) ? SIZER_HOVERED_COLOR : SIZER_COLOR;
        int barX = (int)((getWidth() * sizerProgress) - (sizerWidth / 2f));
        RenderSystem.setShaderColor(barColor.getRed() / 255f, barColor.getGreen() / 255f,
                barColor.getBlue() / 255f, barColor.getAlpha() / 255f);
        RenderSystem.enableBlend();
        fill(matrices, barX, getY(), barX+sizerWidth,getY()+getHeight(), 0xFFFFFFFF);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1,1,1,1);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (mouseInSizer(mouseX, mouseY)) {
                changingSizerPos = true;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (changingSizerPos) {
                changingSizerPos = false;
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (changingSizerPos) {
            sizerProgress = Math.max(Math.min((float) ((mouseX - getX()) / getWidth()), maxSizerProgress), minSizerProgress);
            update();
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
