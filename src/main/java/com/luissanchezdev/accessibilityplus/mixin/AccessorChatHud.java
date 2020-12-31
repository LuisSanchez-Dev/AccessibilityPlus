package com.luissanchezdev.accessibilityplus.mixin;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(ChatHud.class)
public interface AccessorChatHud {
    @Accessor(value = "messages")
    List<ChatHudLine<Text>> getMessages();
}
