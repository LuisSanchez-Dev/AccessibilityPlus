package com.luissanchezdev.accessibilityplus.mixin;

import com.luissanchezdev.accessibilityplus.NarratorPlus;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;

@Mixin(InGameHud.class)
public class TitleInject {
    @Inject(at = @At("HEAD"), method = "setTitles(Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;I;I;I)V")
    public void speakTitle(Text title, Text subtitle, int titleFadeInTicks, int titleRemainTicks, int titleFadeOutTicks, CallbackInfo ci) {
        if(title != null) NarratorPlus.narrate(title.getString());
    }
}