package com.luissanchezdev.accessibilityplus;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.luissanchezdev.accessibilityplus.config.Config;
import com.luissanchezdev.accessibilityplus.gui.ConfigGui;
import com.luissanchezdev.accessibilityplus.gui.ConfigScreen;
import com.luissanchezdev.accessibilityplus.keyboard.KeyboardController;
import com.luissanchezdev.accessibilityplus.mixin.AccessorHandledScreen;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AccessibilityPlus implements ModInitializer {
    public static KeyBinding CONFIG_KEY;
    public static AccessibilityPlus instance;
    public static NarratorPlus narrator;
    public static KeyboardController keyboardController;

    public static int currentColumn = 0;
    public static int currentRow = 0;
    public static boolean isDPressed, isAPressed, isWPressed, isSPressed, isRPressed, isFPressed;
    public static boolean isPointingAtCraftingBlock = false;
	public static Map<String, Integer> mainThreadMap;
	private static CustomWait mainThreadCustomWait;
	private HudScreenHandler hudScreenHandler;

    @Override
    public void onInitialize() {
        instance = this;
        CONFIG_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.config",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.accessibilityplus.general"));

        narrator = new NarratorPlus();
        keyboardController = new KeyboardController();
        System.setProperty("java.awt.headless", "false");
        

		mainThreadMap = new HashMap<String, Integer>();
        mainThreadCustomWait = new CustomWait();
        mainThreadCustomWait.startThread();
        
        hudScreenHandler = new HudScreenHandler();
        
        

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
        	isDPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.d").getCode()));
        	isAPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.a").getCode()));
        	isWPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.w").getCode()));
        	isSPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.s").getCode()));
        	isRPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.r").getCode()));
        	isFPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.f").getCode()));
        	
        	if (client.currentScreen == null) {
        		currentColumn = 0;
        		currentRow = 0;
        	} else {
        		Screen screen =  client.currentScreen;
        		hudScreenHandler.screenHandler(screen);	            	
        	}
        	
            if (client.player == null)
                return;
            

            

            while (CONFIG_KEY.wasPressed()) {
                client.openScreen(new ConfigScreen(new ConfigGui(client.player)));
                return;
            }
            
            
            /* Previous method of getting the item count
               New method in ItemStackTooltipInject.java in /mixin
               
            if (client.currentScreen != null && client.currentScreen instanceof AccessorHandledScreen) {
                Slot hovered = ((AccessorHandledScreen) client.currentScreen).getFocusedSlot();
                if (hovered != null && hovered.hasStack()) {
                    narrator.prefixAmount = String.valueOf(hovered.getStack().getCount()) + " ";
                } else {
                    narrator.prefixAmount = "";
                }
            }
            */
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
                            
                            if(block instanceof CraftingTableBlock) isPointingAtCraftingBlock = true;
                            else isPointingAtCraftingBlock = false;
                            
                            if (!block.equals(narrator.lastBlock) || !blockPos.equals(narrator.lastBlockPos)) {
                                narrator.lastBlock = block;
                                narrator.lastBlockPos = blockPos;
                                String output = "";
                                if (Config.readBlocksEnabled()) {
                                    output += block.getName().getString();
                                }
                                if (blockState.toString().contains("sign") && Config.readSignsContentsEnabled()) {
                                    try {
                                        SignBlockEntity signentity = (SignBlockEntity) client.world
                                                .getBlockEntity(blockPos);
                                        output += " says: ";
                                        output += "1: " + signentity.getTextOnRow(0).getString() + ", ";
                                        output += "2: " + signentity.getTextOnRow(1).getString() + ", ";
                                        output += "3: " + signentity.getTextOnRow(2).getString() + ", ";
                                        output += "4: " + signentity.getTextOnRow(3).getString();
                                    } finally {
                                    }
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
