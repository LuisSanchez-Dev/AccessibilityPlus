package com.luissanchezdev.accessibilityplus;

import com.luissanchezdev.accessibilityplus.mixin.AccessorHandledScreen;
import com.luissanchezdev.accessibilityplus.gui.ConfigScreen;
import com.luissanchezdev.accessibilityplus.gui.ConfigGui;
import com.luissanchezdev.accessibilityplus.keyboard.KeyboardController;
import com.luissanchezdev.accessibilityplus.config.Config;

import me.shedaniel.cloth.api.client.events.v0.ClothClientHooks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class AccessibilityPlus implements ModInitializer {
    public static KeyBinding CONFIG_KEY;
    public static AccessibilityPlus instance;
    public static NarratorPlus narrator;
    public static KeyboardController keyboardController;

    @Override
    public void onInitialize() {
        instance = this;
        CONFIG_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.config",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.accessibilityplus.general"));

        narrator = new NarratorPlus();
        keyboardController = new KeyboardController();
        System.setProperty("java.awt.headless", "false");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;

            while (CONFIG_KEY.wasPressed()) {
                client.openScreen(new ConfigScreen(new ConfigGui(client.player)));
                return;
            }
            if (client.currentScreen != null && client.currentScreen instanceof AccessorHandledScreen) {
                Slot hovered = ((AccessorHandledScreen) client.currentScreen).getFocusedSlot();
                if (hovered != null && hovered.hasStack()) {
                    narrator.prefixAmount = String.valueOf(hovered.getStack().getCount()) + " ";
                } else {
                    narrator.prefixAmount = "";
                }
            }
            if (client.currentScreen == null || !(client.currentScreen instanceof AccessorHandledScreen)) {
                HitResult hit = client.crosshairTarget;
                switch (hit.getType()) {
                    case MISS:
                        // nothing near enough
                        break;
                    case BLOCK:
                        try {
                            BlockHitResult blockHit = (BlockHitResult) hit;
                            BlockPos blockPos = blockHit.getBlockPos();
                            BlockState blockState = client.world.getBlockState(blockPos);

                            Block block = blockState.getBlock();
                            if (!block.equals(narrator.lastBlock) || !blockPos.equals(narrator.lastBlockPos)) {
                                narrator.lastBlock = block;
                                narrator.lastBlockPos = blockPos;
                                String output = "";
                                if (Config.readBlocksEnabled()) {
                                    output += block.getName().getString();
                                }
                                if (blockState.toString().contains("sign") && Config.readSignsContentsEnabled()) {
                                    SignBlockEntity signentity = (SignBlockEntity) client.world
                                            .getBlockEntity(blockPos);
                                    output += " says: ";
                                    output += "1: " + signentity.getTextOnRow(0).getString() + ", ";
                                    output += "2: " + signentity.getTextOnRow(1).getString() + ", ";
                                    output += "3: " + signentity.getTextOnRow(2).getString() + ", ";
                                    output += "4: " + signentity.getTextOnRow(3).getString();
                                }
                                if (!output.equals("")) {
                                    NarratorPlus.narrate(output);
                                }
                            }
                        } finally {
                        }
                        break;
                    case ENTITY:
                        // Entity in range
                        break;
                }
            }
        });

        System.out.println("Hello Fabric world!");
    }
}
