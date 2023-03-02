package org.lexize.ldocs.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lexize.ldocs.LDocs;
import org.lexize.ldocs.LDocsElement;
import org.lexize.ldocs.gui.components.LDocsHSizer;
import org.lexize.ldocs.gui.components.LDocsScrollWidget;
import org.lexize.ldocs.gui.components.LDocsTreeElement;
import org.lexize.ldocs.gui.pages.LDocsPage;

public class LDocsScreen extends Screen {
    private final LDocsTreeElement rootTreeElement;
    public LDocsScreen() {
        super(Component.translatable("ldocs.docs_screen"));
        LDocsElement docsRootElement = new LDocsElement();
        docsRootElement.setElementTitle(Component.translatable("ldocs.tree.root"));
        for (LDocsElement e :
                LDocs.getLoadedElements()) {
            docsRootElement.getChildren().add(e);
        }

        rootTreeElement = docsRootElement.buildTree(0,0,125);
    }
    private LDocsHSizer sizer;
    private LDocsPage selectedPage = null;
    private LDocsScrollWidget pageScrollElement;
    @Override
    protected void init() {
        super.init();
        int wwidth, wheight;
        wwidth = minecraft.getWindow().getGuiScaledWidth();
        wheight = minecraft.getWindow().getGuiScaledHeight();
        LDocsScrollWidget treeScroll = new LDocsScrollWidget(0,0,125, wheight);
        pageScrollElement = new LDocsScrollWidget(0,0,200,wheight);
        treeScroll.setContainedWidget(rootTreeElement);
        sizer = new LDocsHSizer(0,0,wwidth,wheight);
        sizer.setLeftWidget(treeScroll);
        sizer.setRightWidget(pageScrollElement);
        pageScrollElement.setContainedWidget(selectedPage);
        sizer.setMinSizerProgress(0.2f);
        sizer.setMaxSizerProgress(0.4f);
        sizer.setSizerProgress(0.3f);
        addRenderableWidget(sizer);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        LDocsElement element = LDocs.getSelectedElement();
        LDocsPage currentPage = element != null ? element.getElementPage() : null;
        if (currentPage != selectedPage) {
            selectedPage = currentPage;
            pageScrollElement.setContainedWidget(selectedPage);
        }
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
