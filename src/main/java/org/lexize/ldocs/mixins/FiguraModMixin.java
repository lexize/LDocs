package org.lexize.ldocs.mixins;

import org.lexize.ldocs.LDocs;
import org.moon.figura.FiguraMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FiguraMod.class)
public class FiguraModMixin {
    @Inject(method = "onInitializeClient", at = @At("RETURN"), remap = false)
    public void onDocsInit(CallbackInfo ci) {
        LDocs.init();
    }
}
