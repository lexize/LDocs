package org.lexize.ldocs.gui.pages;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.lexize.ldocs.gui.components.LDocsLabel;
import org.lexize.ldocs.gui.components.LDocsWrappedLabel;
import org.moon.figura.lua.docs.FiguraDoc;
import org.moon.figura.lua.docs.FiguraDocsManager;
import org.moon.figura.utils.FiguraText;

import java.util.ArrayList;
import java.util.List;

public class LDocsClassPage extends LDocsPage {
    private static final int DESCRIPTION_PADDING_X = 5;
    private static final int DESCRIPTION_PADDING_Y = 5;
    private final List<AbstractWidget> widgets = new ArrayList<>();
    private final LDocsLabel classNameLabel;
    private final LDocsWrappedLabel descriptionLabel;
    public LDocsClassPage(int x, int y, int width, int height,FiguraDoc.ClassDoc doc) {
        super(x, y, width, height, Component.literal(FiguraDocsManager.getNameFor(doc.thisClass)));
        classNameLabel = new LDocsLabel(5,5, Component.literal(FiguraDocsManager.getNameFor(doc.thisClass)));
        classNameLabel.setTextSize(3);
        widgets.add(classNameLabel);
        descriptionLabel = new LDocsWrappedLabel(DESCRIPTION_PADDING_X,
                classNameLabel.getHeight()+5+DESCRIPTION_PADDING_Y, width-(DESCRIPTION_PADDING_X*2),
                FiguraText.of("docs."+doc.description));
        widgets.add(descriptionLabel);
    }

    @Override
    protected List<? extends AbstractWidget> getContainedChildren() {
        return widgets;
    }

    @Override
    public void resize(int width) {
        descriptionLabel.setWidth(width-(DESCRIPTION_PADDING_X*2));
    }
}
