package org.lexize.ldocs.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.lexize.ldocs.utils.LDocsClickEvent;
import org.lexize.ldocs.utils.LDocsDrawHelper;

public class LDocsLabel extends AbstractWidget {
    private float textSize = 1;
    public LDocsLabel(int x, int y, Component message) {
        super(x, y, 1, 1, message);
    }
    protected Minecraft getClient() {
        return Minecraft.getInstance();
    }
    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, getMessage());
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        LDocsDrawHelper.drawText(matrices, getMessage(), this.getX(), this.getY(), 0, textSize, 0xFFFFFF);
    }

    @Override
    public int getHeight() {
        var font = getClient().font;
        return (int) (font.split(getMessage(), font.width(getMessage())).size() * font.lineHeight * textSize);
    }

    @Override
    public int getWidth() {
        return (int) (getClient().font.width(getMessage()) * textSize);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var client = getClient();
        var splitter = client.font.getSplitter();
        var lines = splitter.splitLines(getMessage(), Integer.MAX_VALUE, Style.EMPTY);
        int x = (int) Math.floor((mouseX - getX()) / textSize);
        int y = (int) Math.floor((mouseY - getY()) / textSize);
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
