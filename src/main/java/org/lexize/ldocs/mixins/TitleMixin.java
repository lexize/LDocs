package org.lexize.ldocs.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lexize.ldocs.gui.LDocsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleMixin extends Screen {
    @Unique
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("ldocs","textures/gui/docs_button.png");
    protected TitleMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {

        var button = new ImageButton(0,0,20,20,0,0, 20, BUTTON_TEXTURE, 64,64, TitleMixin::onPress);
        this.addRenderableWidget(button);
    }

    private static void onPress(Button btn) {
        Minecraft.getInstance().setScreen(new LDocsScreen());
    }
}
