package org.lexize.ldocs.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.lexize.ldocs.utils.LDocsDrawHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LDocsWrappedLabel extends LDocsLabel {
    private List<FormattedCharSequence> renderableSequence = new ArrayList<>();
    int width;
    private boolean shouldUpdate = false;
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
        width = value;
        shouldUpdate = true;
    }

    @Override
    public void setTextSize(float textSize) {
        super.setTextSize(textSize);
        shouldUpdate = true;
    }

    @Override
    public int getHeight() {
        var font = getClient().font;
        return (int) (font.split(getMessage(), font.width(getMessage())).size() * font.lineHeight * getTextSize());
    }

    private void updateRenderableSequence() {
        renderableSequence.clear();
        renderableSequence.addAll(LDocsDrawHelper.wrapText(getMessage(), getWidth(), getTextSize()));
        shouldUpdate = false;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (shouldUpdate) updateRenderableSequence();
        float y = 0;
        for (Iterator<FormattedCharSequence> sequenceIterator = renderableSequence.iterator(); sequenceIterator.hasNext(); y += (getClient().font.lineHeight * getTextSize())) {
            LDocsDrawHelper.drawText(matrices, sequenceIterator.next(), getX(), getY()+y,0,getTextSize(), 0xFFFFFF);
        }
    }
}
