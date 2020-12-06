package com.luissanchezdev.accessibilityplus;

import com.luissanchezdev.accessibilityplus.mixin.AccessorHandledScreen;
import com.luissanchezdev.accessibilityplus.keyboard.KeyboardController;

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
    public static AccessibilityPlus instance;
    public static NarratorPlus narrator;
    public static KeyboardController keyboardController;

    @Override
    public void onInitialize() {
        instance = this;
        narrator = new NarratorPlus();
        keyboardController = new KeyboardController();
        System.setProperty("java.awt.headless", "false");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;

            if (client.currentScreen != null && client.currentScreen instanceof AccessorHandledScreen) {
                Slot hovered = ((AccessorHandledScreen) client.currentScreen).getFocusedSlot();
                if (hovered != null && hovered.hasStack()) {
                    narrator.prefixAmount = String.valueOf(hovered.getStack().getCount()) + " ";
                } else {
                    narrator.prefixAmount = "";
                }
            }
            if (client.currentScreen == null || !(client.currentScreen instanceof AccessorHandledScreen)) {
                // System.setProperty("java.library.path", myLibraryPath);
                // client.player.sendMessage(new
                // net.minecraft.text.LiteralText(System.getProperty("java.library.path")),
                // false);
                // ;
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
                                String output = block.getName().getString();
                                if (blockState.toString().contains("sign")) {
                                    SignBlockEntity signentity = (SignBlockEntity) client.world
                                            .getBlockEntity(blockPos);
                                    output += " says: ";
                                    output += "1: " + signentity.getTextOnRow(0).getString() + ", ";
                                    output += "2: " + signentity.getTextOnRow(1).getString() + ", ";
                                    output += "3: " + signentity.getTextOnRow(2).getString() + ", ";
                                    output += "4: " + signentity.getTextOnRow(3).getString();
                                }
                                NarratorPlus.narrate(output);
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
