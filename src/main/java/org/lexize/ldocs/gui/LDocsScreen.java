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
import org.lexize.ldocs.gui.components.ScrollBarWidgetFixed;
import org.lexize.ldocs.gui.pages.LDocsPage;
import org.moon.figura.gui.screens.AbstractPanelScreen;
import org.moon.figura.gui.widgets.ScrollBarWidget;
import org.moon.figura.gui.widgets.TextField;

import java.util.function.Function;

public class LDocsScreen extends AbstractPanelScreen {
    private final LDocsTreeElement rootTreeElement;
    public static final Component TITLE = Component.translatable("ldocs.docs_screen.title");
    public LDocsScreen(Screen parent) {
        super(parent, TITLE, LDocsScreen.class);
        LDocsElement docsRootElement = new LDocsElement();
        docsRootElement.setElementTitle(Component.translatable("ldocs.tree.root"));
        for (LDocsElement e :
                LDocs.getLoadedElements()) {
            docsRootElement.getChildren().add(e);
        }

        rootTreeElement = docsRootElement.buildTree(0,0,125);
    }
    private LDocsHSizer sizer;
    private TextField searchField = null;
    private String searchText = "";
    private LDocsPage selectedPage = null;
    private LDocsElement selectedElement = null;
    private LDocsScrollWidget pageScrollElement;
    private ScrollBarWidgetFixed treeScrollBarWidget;
    private ScrollBarWidget.OnPress onTreeScrollBarAct;
    private ScrollBarWidgetFixed pageScrollBarWidget;
    private ScrollBarWidget.OnPress onPageScrollBarAct;
    private LDocsScrollWidget treeScroll;
    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    protected void init() {
        super.init();
        int wwidth, wheight;
        wwidth = minecraft.getWindow().getGuiScaledWidth();
        wheight = minecraft.getWindow().getGuiScaledHeight();
        int scrollBarSize = 10;
        if (searchField == null) {
            searchField = new TextField(0, panels.height, 200,20, TextField.HintType.SEARCH, null);
            searchField.getField().setValue(searchText);
        }
        int yOffset = searchField.y + searchField.height;
        addRenderableWidget(searchField);
        treeScroll = new LDocsScrollWidget(scrollBarSize,yOffset,125-scrollBarSize, wheight-yOffset);
        pageScrollElement = new LDocsScrollWidget(scrollBarSize,yOffset,200,wheight-yOffset);
        treeScroll.setContainedWidget(rootTreeElement);
        treeScrollBarWidget = new ScrollBarWidgetFixed(0,yOffset,scrollBarSize,wheight-yOffset, 0);
        onTreeScrollBarAct = (s) -> treeScroll.setScroll(treeScroll.maxScroll() * s.getScrollProgress());
        treeScrollBarWidget.setAction(onTreeScrollBarAct);
        addRenderableWidget(treeScrollBarWidget);
        pageScrollBarWidget = new ScrollBarWidgetFixed(wwidth-scrollBarSize, yOffset, scrollBarSize, wheight-yOffset, 0);
        onPageScrollBarAct = (s) -> pageScrollElement.setScroll(pageScrollElement.maxScroll() * s.getScrollProgress());
        addRenderableWidget(pageScrollBarWidget);
        sizer = new LDocsHSizer(scrollBarSize,yOffset,wwidth-(scrollBarSize*2),wheight-yOffset);
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
        if (element != selectedElement) {
            selectedElement = element;
            selectedPage = element.getElementPage();
            pageScrollElement.setContainedWidget(selectedPage);
            pageScrollElement.setScroll(0);
        }
        searchField.width = sizer.getLeftWidth() + 10;
        treeScrollBarWidget.setAction(null);
        treeScrollBarWidget.setScrollProgress(treeScroll.getScroll() / Math.max(treeScroll.maxScroll(), 1));
        treeScrollBarWidget.setAction(onTreeScrollBarAct);
        pageScrollBarWidget.setAction(null);
        pageScrollBarWidget.setScrollProgress(pageScrollElement.getScroll() / Math.max(pageScrollElement.maxScroll(), 1));
        pageScrollBarWidget.setAction(onPageScrollBarAct);
        if (!searchText.equalsIgnoreCase(searchField.getField().getValue())) {
            searchText = searchField.getField().getValue();
            iterateOverTree(rootTreeElement, this::searchCheck);
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
    private boolean searchCheck(LDocsTreeElement e) {
        String s = e.getSourceElement().getElementTitle().getString();
        return s.toLowerCase().contains(searchText.toLowerCase());
    }
    private boolean iterateOverTree(LDocsTreeElement element, Function<LDocsTreeElement, Boolean> predicate) {
        boolean anyMatch = false;
        for (var e :
                element.getChildren()) {
            boolean result = iterateOverTree(e, predicate);
            e.visible = result;
            e.active = result;
            anyMatch = anyMatch || result;
        }
        return anyMatch || predicate.apply(element);
    }
}
