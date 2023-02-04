package org.lexize.ldocs;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.lexize.ldocs.gui.LDocsPage;
import org.lexize.ldocs.gui.LDocsTreeElement;

import java.util.ArrayList;
import java.util.List;

public abstract class LDocsElement {
    private Component elementTitle;
    private List<LDocsElement> children = new ArrayList<>();
    private LDocsPage elementPage;
    /**
     * Should docs element be expanded in docs screen by default.
     */
    public boolean expandedByDefault = false;
    public LDocsTreeElement buildTree(int x, int y, int width) {
        LDocsTreeElement element = new LDocsTreeElement(x,y,width, this);
        List<LDocsTreeElement> children = new ArrayList<>();
        for (LDocsElement e :
                this.children) {
            children.add(e.buildTree(0,0,width));
        }
        element.setChildren(children);
        return element;
    }

    public Component getElementTitle() {
        return elementTitle;
    }

    public List<LDocsElement> getChildren() {
        return children;
    }

    public LDocsPage getElementPage() {
        return elementPage;
    }
}
