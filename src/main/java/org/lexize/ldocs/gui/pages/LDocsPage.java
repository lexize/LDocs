package org.lexize.ldocs.gui.pages;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class LDocsPage extends AbstractContainerWidget {
    private int scroll;
    public LDocsPage(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
    }

    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        matrices.pushPose();
        matrices.translate(0,-scroll,0);
        super.render(matrices, mouseX, mouseY, delta);
        matrices.popPose();
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        matrices.pushPose();
        matrices.translate(0,-scroll,0);
        super.renderButton(matrices, mouseX, mouseY, delta);
        matrices.popPose();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY+scroll);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY+scroll);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY+scroll, amount);
    }

    @Override
    public @NotNull Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        return super.getChildAt(mouseX, mouseY+scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY+scroll, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY+scroll, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY+scroll, button, deltaX, deltaY);
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    public int getScroll() {
        return scroll;
    }
}
