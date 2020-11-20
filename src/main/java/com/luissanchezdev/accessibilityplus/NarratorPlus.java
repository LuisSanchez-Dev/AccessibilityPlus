package com.luissanchezdev.accessibilityplus;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class NarratorPlus {
    public String lastText = "";
    public String prefixAmount = "";
    public Block lastBlock = null;
    public BlockPos lastBlockPos = null;
    private static NarratorPlus instance;

    public NarratorPlus() {
        instance = this;
    }

    public static void narrate(String text) {
        NarratorManager.INSTANCE.narrate(text);
    }

    public static NarratorPlus getInstance() {
        return instance;
    }
}
