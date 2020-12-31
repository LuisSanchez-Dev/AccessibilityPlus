package com.luissanchezdev.accessibilityplus.keyboard;

import com.luissanchezdev.accessibilityplus.NarratorPlus;
import com.luissanchezdev.accessibilityplus.mixin.AccessorChatHud;

import java.util.List;
import java.util.ArrayList;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

import com.luissanchezdev.accessibilityplus.NarratorPlus;

import org.lwjgl.glfw.GLFW;

public class ChatReader {
    private static ChatReader instance;
    private static KeyBinding UP_KEY;
    private static KeyBinding DOWN_KEY;
    private static KeyBinding MOST_RECENT_KEY;
    private static int currentLine = 0;

    public ChatReader() {
        instance = this;
        UP_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.chat.up",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_UP, "key.categories.accessibilityplus.inventorycontrol"));
        DOWN_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.chat.down",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_DOWN, "key.categories.accessibilityplus.inventorycontrol"));
        MOST_RECENT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.chat.recent",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_END, "key.categories.accessibilityplus.inventorycontrol"));
        ClientTickEvents.END_CLIENT_TICK.register(ChatReader::onTick);
    }

    private static void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        List<ChatHudLine<Text>> messages = ((AccessorChatHud) client.inGameHud.getChatHud()).getMessages();
        if (messages.size() <= 0) {
            NarratorPlus.narrate("No messages");
            return;
        }

        while (MOST_RECENT_KEY.wasPressed()) {
            currentLine = 0;
            NarratorPlus.narrate(messages.get(0).getText().getString());
            return;
        }
        while (DOWN_KEY.wasPressed()) {
            if (currentLine <= 0) {
                NarratorPlus.narrate(messages.get(0).getText().getString());
                return;
            } else {
                currentLine--;
                NarratorPlus.narrate(messages.get(currentLine).getText().getString());
                return;
            }
        }
        while (UP_KEY.wasPressed()) {
            if (currentLine >= messages.size() - 1) {
                NarratorPlus.narrate(messages.get(messages.size() - 1).getText().getString());
                return;
            } else {
                currentLine++;
                NarratorPlus.narrate(messages.get(currentLine).getText().getString());
                return;
            }
        }
    }
}
