package org.lexize.ldocs.gui;

import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class LDocsAbstractScrollWidget extends AbstractContainerWidget {
    private final List<? extends AbstractWidget> children = new ArrayList<>();
    public LDocsAbstractScrollWidget(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
    }

    @Override
    protected List<? extends AbstractWidget> getContainedChildren() {
        return children;
    }
}
