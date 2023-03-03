package org.lexize.ldocs.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.lexize.ldocs.utils.LDocsClickEvent;
import org.lexize.ldocs.utils.LDocsDrawHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LDocsWrappedLabel extends LDocsLabel {
    private List<FormattedCharSequence> renderableSequence = new ArrayList<>();
    int width;
    private boolean shouldUpdate;
    public LDocsWrappedLabel(int x, int y, int width, Component message) {
        super(x, y, message);
        setWidth(width);
        this.width = width;
        shouldUpdate = true;
    }
    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, getMessage());
    }

    @Override
    public void setMessage(Component message) {
        super.setMessage(message);
        shouldUpdate = true;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int value) {
        if (width != value) {
            width = value;
            shouldUpdate = true;
        }
    }

    @Override
    public void setTextSize(float textSize) {
        if (textSize != getTextSize()) {
            super.setTextSize(textSize);
            shouldUpdate = true;
        }
    }

    @Override
    public int getHeight() {
        var font = getClient().font;
        return (int) (renderableSequence.size() * font.lineHeight * getTextSize());
    }

    public void update() {
        renderableSequence.clear();
        renderableSequence.addAll(LDocsDrawHelper.wrapText(getMessage(), getWidth(), getTextSize()));
        shouldUpdate = false;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (shouldUpdate) update();
        float y = 0;
        for (Iterator<FormattedCharSequence> sequenceIterator = renderableSequence.iterator(); sequenceIterator.hasNext(); y += (getClient().font.lineHeight * getTextSize())) {
            LDocsDrawHelper.drawText(matrices, sequenceIterator.next(), getX(), getY()+y,0,getTextSize(), 0xFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var client = getClient();
        var splitter = client.font.getSplitter();
        var lines = renderableSequence;
        int x = (int) Math.floor((mouseX - getX()) / getTextSize());
        int y = (int) Math.floor((mouseY - getY()) / getTextSize());
        if (x >= 0 && y >= 0 && y / 9 < lines.size()) {
            var line = lines.get(y/9);
            var style = splitter.componentStyleAtWidth(line, x);
            if (style != null) {
                var clickEvent = style.getClickEvent();
                if (clickEvent instanceof LDocsClickEvent ldce) {
                    ldce.onClickHandler.run();
                    return true;
                }
                else if (client.screen != null) {
                    client.screen.handleComponentClicked(style);
                    return true;
                }
            }
        }
        return false;
    }
}
