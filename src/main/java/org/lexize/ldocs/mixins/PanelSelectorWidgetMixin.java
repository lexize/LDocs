package org.lexize.ldocs.mixins;

import net.minecraft.client.gui.screens.Screen;
import org.lexize.ldocs.gui.LDocsScreen;
import org.moon.figura.gui.screens.AbstractPanelScreen;
import org.moon.figura.gui.widgets.PanelSelectorWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(value = PanelSelectorWidget.class, remap = false)
public class PanelSelectorWidgetMixin {
    @Shadow @Final @Mutable
    private static List<Function<Screen, AbstractPanelScreen>> PANELS;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void addLDocsScreen(CallbackInfo ci) {
        ArrayList<Function<Screen, AbstractPanelScreen>> panels = new ArrayList<>(PANELS);
        panels.add(LDocsScreen::new);
        PANELS = panels;
    }
}
