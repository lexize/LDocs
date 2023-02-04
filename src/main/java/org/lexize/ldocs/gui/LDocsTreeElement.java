package org.lexize.ldocs.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lexize.ldocs.LDocs;
import org.lexize.ldocs.LDocsElement;
import org.lexize.ldocs.utils.LDocsDrawHelper;

import java.awt.*;
import java.util.List;

public class LDocsTreeElement extends AbstractWidget {
    private static final ResourceLocation EXPANDER_ICON = new ResourceLocation("ldocs", "textures/gui/expander_icon.png");
    private static final Color TREE_ELEMENT_COLOR = null;
    private static final Color TREE_ELEMENT_HOVER_COLOR = null;
    private static final Color TREE_ELEMENT_SELECTED_COLOR = null;
    private static final Color TREE_ELEMENT_SELECTED_HOVER_COLOR = null;
    private static final Color TREE_ELEMENT_EXPANDER_COLOR = null;
    private static final Color TREE_ELEMENT_EXPANDER_HOVER_COLOR = null;
    private float expandedProgress = 0;
    private LDocsElement sourceElement;
    private List<LDocsTreeElement> children;
    private boolean expanded;
    public LDocsTreeElement(int x, int y, int width, LDocsElement sourceElement) {
        super(x, y, width, 1, sourceElement.getElementTitle());
        this.sourceElement = sourceElement;
        expanded = sourceElement.expandedByDefault;
    }

    public void setChildren(List<LDocsTreeElement> element) {
        children = element;
    }

    public List<LDocsTreeElement> getChildren() {
        return children;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {

    }
    private boolean isMouseInExpander(int mouseX, int mouseY) {
        return false;
    }
    private boolean isMouseInElement(int mouseX, int mouseY) {
        return (mouseX >= getX() && mouseX <= getX()+getWidth()) &&
                (mouseY >= getY() && mouseY <= getY()+getHeight());
    }

    private boolean canBeExpanded () {
        return children != null && children.size() > 0;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        boolean canBeExpanded = canBeExpanded();
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
        LDocsDrawHelper.fill(matrices,x,y,getWidth(),25, pcr, pcg, pcb, pca);
        int width = getWidth();
        int textStartPos = 8 + (canBeExpanded ? 25 : 0);
        if (canBeExpanded) {
            Color ec = isMouseInExpander(mouseX, mouseY) ? TREE_ELEMENT_EXPANDER_HOVER_COLOR : TREE_ELEMENT_EXPANDER_COLOR;
            float ecr, ecg, ecb, eca;
            ecr = ec.getRed() / 255f;
            ecg = ec.getGreen() / 255f;
            ecb = ec.getBlue() / 255f;
            eca = ec.getAlpha() / 255f;
            LDocsDrawHelper.fillImageRotated(matrices, x,y,25,25,(float)(Math.toRadians(expandedProgress*90)),0,0,1,1,ecr,ecg,ecb,eca);
        }
        expandedProgress = expanded ? Math.min(expandedProgress+delta, 1) : Math.max(expandedProgress - delta, 0);
    }
}
