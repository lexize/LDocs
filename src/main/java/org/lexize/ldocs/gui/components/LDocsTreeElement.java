package org.lexize.ldocs.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.lexize.ldocs.LDocs;
import org.lexize.ldocs.LDocsElement;
import org.lexize.ldocs.utils.LDocsDrawHelper;
import org.moon.figura.utils.ColorUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LDocsTreeElement extends AbstractContainerWidget {
    private static final int EXPANDER_WIDTH = 25;
    private static final int EXPANDER_HEIGHT = 25;
    private static final int CHILDREN_ELEMENT_X_PADDING = 5;
    private static final int CHILDREN_ELEMENT_Y_PADDING = 2;
    private static final List<? extends AbstractWidget> EMPTY_WIDGET_LIST = new ArrayList<>();
    private static final ResourceLocation EXPANDER_ICON = new ResourceLocation("ldocs", "textures/gui/expander_icon.png");
    private static final Color TREE_ELEMENT_COLOR = new Color(1f,1f,1f,0.1f);
    private static final Color TREE_ELEMENT_HOVER_COLOR = new Color(1f,1f,1f,0.25f);
    private static final Color TREE_ELEMENT_SELECTED_COLOR;
    private static final Color TREE_ELEMENT_SELECTED_HOVER_COLOR;
    private static final Color TREE_ELEMENT_EXPANDER_COLOR = Color.white;
    private static final Color TREE_ELEMENT_EXPANDER_HOVER_COLOR;
    static {
        var selectedCol = ColorUtils.Colors.MAYA_BLUE.vec.asVec3f();
        TREE_ELEMENT_SELECTED_COLOR = new Color(selectedCol.x,selectedCol.y,selectedCol.z,0.1f);
        TREE_ELEMENT_SELECTED_HOVER_COLOR = new Color(selectedCol.x,selectedCol.y,selectedCol.z,0.25f);
        TREE_ELEMENT_EXPANDER_HOVER_COLOR = new Color(selectedCol.x,selectedCol.y,selectedCol.z,1f);
    }
    private float expandedProgress = 0;
    private final LDocsElement sourceElement;
    private final List<LDocsTreeElement> children = new ArrayList<>();
    private boolean expanded;
    private int prevTitleWidth = -1;
    private final List<FormattedCharSequence> elementTitle = new ArrayList<>();
    public LDocsTreeElement(int x, int y, int width, LDocsElement sourceElement) {
        super(x, y, width, 1, sourceElement.getElementTitle());
        this.sourceElement = sourceElement;
        expanded = sourceElement.expandedByDefault;
    }

    public List<LDocsTreeElement> getChildren() {
        return children;
    }

    @Override
    protected List<? extends AbstractWidget> getContainedChildren() {
        return expanded ? children : EMPTY_WIDGET_LIST;
    }

    private void updateElementTitle(int titleWidth) {
        if (titleWidth != prevTitleWidth) {
            prevTitleWidth = titleWidth;
            elementTitle.clear();
            elementTitle.addAll(LDocsDrawHelper.wrapText(sourceElement.getElementTitle(), titleWidth, 1));
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput builder) {

    }
    private boolean isMouseInExpander(double mouseX, double mouseY) {
        return (mouseX >= getX() && mouseX <= getX()+EXPANDER_WIDTH) &&
                (mouseY >= getY() && mouseY <= getY()+getActualHeight());
    }
    private boolean isMouseInElement(double mouseX, double mouseY) {
        return (mouseX >= getX() && mouseX <= getX()+getWidth()) &&
                (mouseY >= getY() && mouseY <= getY()+getActualHeight());
    }

    private boolean canBeExpanded () {
        return children.size() > 0;
    }
    public int getActualHeight() {
        return Math.max(25, (elementTitle.size() * 9)+16);
    }
    @Override
    public int getHeight() {
        int elementHeight = getActualHeight();
        if (!expanded) return elementHeight;
        int heightSum = 0;
        for (var element :
                children) {
            if (element.visible) {
                heightSum += element.getHeight() + CHILDREN_ELEMENT_Y_PADDING;
            }
        }
        return elementHeight + heightSum;
    }
    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        boolean canBeExpanded = canBeExpanded();
        int width = getWidth();
        int textStartPos = 8 + (canBeExpanded ? EXPANDER_WIDTH : 0);
        int textWidth = (width - 16) - (canBeExpanded ? EXPANDER_WIDTH : 0);
        updateElementTitle(textWidth);
        boolean isSelected = sourceElement == LDocs.getSelectedElement();
        Color pc = !isMouseInElement(mouseX, mouseY) ? (
                    !isSelected ? TREE_ELEMENT_COLOR : TREE_ELEMENT_SELECTED_COLOR
                ) : (
                    !isSelected ? TREE_ELEMENT_HOVER_COLOR : TREE_ELEMENT_SELECTED_HOVER_COLOR
                );
        int x = getX();
        int y = getY();
        float pcr, pcg, pcb, pca;
        pcr = pc.getRed() / 255f;
        pcg = pc.getGreen() / 255f;
        pcb = pc.getBlue() / 255f;
        pca = pc.getAlpha() / 255f;
        LDocsDrawHelper.fill(matrices,x,y,getWidth(),getActualHeight(), pcr, pcg, pcb, pca);
        if (canBeExpanded) {
            Color ec = isMouseInExpander(mouseX, mouseY) ? TREE_ELEMENT_EXPANDER_HOVER_COLOR : TREE_ELEMENT_EXPANDER_COLOR;
            float ecr, ecg, ecb, eca;
            ecr = ec.getRed() / 255f;
            ecg = ec.getGreen() / 255f;
            ecb = ec.getBlue() / 255f;
            eca = ec.getAlpha() / 255f;
            RenderSystem.setShaderTexture(0, EXPANDER_ICON);
            int expanderYOffset = (getActualHeight() - EXPANDER_HEIGHT)/2;
            LDocsDrawHelper.fillImageRotated(matrices, x,y+expanderYOffset,EXPANDER_WIDTH,EXPANDER_HEIGHT,(float)(Math.toRadians(expandedProgress*90)),0,0,1,1,ecr,ecg,ecb,eca);
        }
        expandedProgress = expanded ? Math.min(expandedProgress+(delta/2), 1) : Math.max(expandedProgress - (delta/2), 0);
        if (expanded) {
            int xOffset = CHILDREN_ELEMENT_X_PADDING;
            int yOffset = getActualHeight() + CHILDREN_ELEMENT_Y_PADDING;
            for (var element :
                    children) {
                if (element.visible) {
                    element.setX(getX() + xOffset);
                    element.setY(getY()+yOffset);
                    element.setWidth(getWidth()-CHILDREN_ELEMENT_X_PADDING);
                    yOffset += element.getHeight() + CHILDREN_ELEMENT_Y_PADDING;
                }
            }
        }
        for (int i = 0; i < elementTitle.size(); i++) {
            LDocsDrawHelper.drawText(matrices,elementTitle.get(i),x+textStartPos,y+8+(i*9),0,1, 0xFFFFFFFF);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseInElement(mouseX, mouseY)) {
            if (isMouseInExpander(mouseX, mouseY) && canBeExpanded()) {
                expanded = !expanded;
            }
            else {
                LDocs.setSelectedElement(sourceElement);
            }
            return true;
        }
        for(var e: children) {
            if (e.active && e.visible && e.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        for (var e :
                children) {
            if (e.visible) {
                e.render(matrices, mouseX, mouseY, delta);
            }
        }
    }

    public LDocsElement getSourceElement() {
        return sourceElement;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded && canBeExpanded();
    }
}
