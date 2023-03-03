package org.lexize.ldocs.gui.pages;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class LDocsPage extends AbstractContainerWidget {
    public LDocsPage(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
    }
    @Override
    public void setWidth(int value) {
        super.setWidth(value);
    }
}
