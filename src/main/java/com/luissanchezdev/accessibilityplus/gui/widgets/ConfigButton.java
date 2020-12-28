package com.luissanchezdev.accessibilityplus.gui.widgets;

import com.luissanchezdev.accessibilityplus.config.Config;

import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.client.util.math.MatrixStack;
import com.luissanchezdev.accessibilityplus.NarratorPlus;
import net.minecraft.text.Text;

import net.minecraft.text.TranslatableText;

public class ConfigButton extends WButton {
    private String translateKey;
    private String jsonKey;

    public ConfigButton(String translationKey, String jsonKey) {
        super(new TranslatableText(translationKey, Config.get(jsonKey) ? "on" : "off"));
        this.translateKey = translationKey;
        this.jsonKey = jsonKey;

    }

    @Override
    public void onClick(int x, int y, int button) {
        super.onClick(x, y, button);
        boolean enabled = Config.toggle(this.jsonKey);
        TranslatableText newButtonText = new TranslatableText(this.translateKey, enabled ? "on" : "off");
        this.setLabel(newButtonText);
        NarratorPlus.narrate(this.getLabel().getString());
        // Toggle the value
        // Save hte value

    }
}
