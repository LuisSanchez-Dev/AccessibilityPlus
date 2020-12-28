package com.luissanchezdev.accessibilityplus.gui;

import io.github.cottonmc.cotton.gui.widget.*;

import com.luissanchezdev.accessibilityplus.NarratorPlus;
import com.luissanchezdev.accessibilityplus.gui.widgets.APButton;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class ConfigScreen extends CottonClientScreen {
    private WWidget spokenElement;

    public ConfigScreen(GuiDescription description) {
        super(new TranslatableText("gui.accessibilityplus.config.title"), description);
        Element focus = this.getFocused();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);

        WWidget focusedElement = this.description.getFocus();
        if (focusedElement == null || focusedElement == this.spokenElement) {
            return;
        }

        this.spokenElement = focusedElement;

        if (focusedElement instanceof WButton) {
            NarratorPlus.narrate(((WButton) focusedElement).getLabel().getString());
        }
    }
}