package org.lexize.ldocs.gui.pages;

import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.moon.figura.lua.docs.FiguraDoc;
import org.moon.figura.lua.docs.FiguraDocsManager;

import java.util.List;

public class LDocsClassPage extends LDocsPage {
    public LDocsClassPage(int x, int y, int width, int height,FiguraDoc.ClassDoc doc) {
        super(x, y, width, height, Component.literal(FiguraDocsManager.getNameFor(doc.thisClass)));
    }

    @Override
    protected List<? extends AbstractWidget> getContainedChildren() {
        return null;
    }
}
