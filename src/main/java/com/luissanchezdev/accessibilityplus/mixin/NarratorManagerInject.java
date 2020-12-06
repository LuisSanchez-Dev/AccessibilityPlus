package com.luissanchezdev.accessibilityplus.mixin;

import com.luissanchezdev.accessibilityplus.NarratorPlus;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.util.NarratorManager;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NarratorManager.class)
public class NarratorManagerInject {

    @Inject(at = @At("HEAD"), method = "narrate(ZLjava/lang/String;)V", cancellable = true)
    public void sayWithNVDA(boolean interrupt, String message, CallbackInfo ci) {
        if (NarratorPlus.isNVDALoaded()) {
            NarratorPlus.narrate(message);
            ci.cancel();
        }
    }
}