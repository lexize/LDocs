package org.lexize.ldocs;

import net.minecraft.network.chat.Component;
import org.lexize.ldocs.gui.pages.LDocsPage;
import org.lexize.ldocs.gui.components.LDocsTreeElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LDocsElement {
    private Component elementTitle;
    private List<LDocsElement> children = new ArrayList<>();
    /**
     * Should docs element be expanded in docs screen by default.
     */
    public boolean expandedByDefault = false;
    private Supplier<LDocsPage> elementPage;
    public LDocsTreeElement buildTree(int x, int y, int width) {
        LDocsTreeElement element = new LDocsTreeElement(x,y,width, this);
        List<LDocsTreeElement> children = new ArrayList<>();
        for (LDocsElement e :
                this.children) {
            children.add(e.buildTree(0,0,width));
        }
        element.getChildren().addAll(children);
        return element;
    }

    public void setElementTitle(Component elementTitle) {
        this.elementTitle = elementTitle;
    }

    public Component getElementTitle() {
        return elementTitle;
    }

    public List<LDocsElement> getChildren() {
        return children;
    }

    public LDocsPage getElementPage() {
        return elementPage.get();
    }

    public void setElementPage(Supplier<LDocsPage> elementPage) {
        this.elementPage = elementPage;
    }
}
