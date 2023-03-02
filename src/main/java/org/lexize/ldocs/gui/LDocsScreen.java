package org.lexize.ldocs.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lexize.ldocs.LDocs;
import org.lexize.ldocs.LDocsElement;
import org.lexize.ldocs.gui.components.LDocsScrollWidget;
import org.moon.figura.FiguraMod;
import org.moon.figura.lua.docs.FiguraDoc;

public class LDocsScreen extends Screen {
    public LDocsScreen() {
        super(Component.translatable("ldocs.docs_screen"));
    }
    @Override
    protected void init() {
        super.init();
        LDocsElement docsRootElement = new LDocsElement();
        docsRootElement.setElementTitle(Component.translatable("ldocs.tree.root"));
        for (LDocsElement e :
                LDocs.getLoadedElements()) {
            docsRootElement.getChildren().add(e);
        }
        var tree = docsRootElement.buildTree(0,0,125);
        LDocsScrollWidget treeScroll = new LDocsScrollWidget(0,0,125, minecraft.getWindow().getGuiScaledHeight());
        treeScroll.setContainedWidget(tree);
        addRenderableWidget(treeScroll);
        System.out.printf("%s %s",treeScroll.getWidth(), treeScroll.getHeight());
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        boolean s = false;
        for (GuiEventListener gel :
                this.children()) {
            s = gel.mouseScrolled(mouseX, mouseY, amount) || s;
        }
        return super.mouseScrolled(mouseX, mouseY, amount) || s;
    }
}
