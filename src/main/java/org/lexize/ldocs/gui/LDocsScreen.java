package org.lexize.ldocs.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lexize.ldocs.utils.LDocsDrawHelper;

public class LDocsScreen extends Screen {
    public LDocsScreen() {
        super(Component.translatable("ldocs.docs_screen"));
    }
    private float rot = 0;
    @Override
    protected void init() {
        super.init();
        //int width = minecraft.getWindow().getGuiScaledWidth();
        //int height = minecraft.getWindow().getGuiScaledHeight();
        //LDocsHSizer sizer = new LDocsHSizer(0,0, width, height);
        //LDocsWrappedLabel testLabel1 = new LDocsWrappedLabel(0,0,150, Component.literal("Very long string to test label text wrapping"));
        //testLabel1.setTextSize(2.5f);
        //LDocsWrappedLabel testLabel2 = new LDocsWrappedLabel(0,0,150, Component.literal("Very long string to test label text wrapping"));
        //testLabel2.setTextSize(2.5f);
        //sizer.setLeftWidget(testLabel1);
        //sizer.setRightWidget(testLabel2);
        //sizer.update();
        //sizer.setMaxSizerProgress(0.5f);
        //addRenderableWidget(sizer);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
