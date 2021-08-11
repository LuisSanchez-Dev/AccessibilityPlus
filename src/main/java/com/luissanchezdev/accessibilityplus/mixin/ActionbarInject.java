package com.luissanchezdev.accessibilityplus.mixin;

import com.luissanchezdev.accessibilityplus.config.Config;

import com.luissanchezdev.accessibilityplus.NarratorPlus;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;

@Mixin(InGameHud.class)
public class ActionbarInject {
    @Inject(at = @At("HEAD"), method = "setOverlayMessage(Lnet/minecraft/text/Text;Z)V")
    public void speakActionbar(Text message, boolean tinted, CallbackInfo ci) {
        if(Config.get(Config.ATION_BAR_KEY)){
            NarratorPlus.narrate(message.getString());
        }
    }
}
