package com.luissanchezdev.accessibilityplus.mixin;

import java.util.List;

import com.luissanchezdev.accessibilityplus.NarratorPlus;
import com.luissanchezdev.accessibilityplus.keyboard.KeyboardController;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@Mixin(value = ItemStack.class, priority = 0)
public class ItemStackTooltipInject {
  @Inject(at = @At("RETURN"), method = "getTooltip")
  private void getTooltipMixin(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info)
      throws Exception {
    if (MinecraftClient.getInstance().world == null)
      return;
    ItemStack itemStack = (ItemStack) ((Object) this);
    List<Text> list = info.getReturnValue();

    addCount: {
      if (!itemStack.isStackable() || itemStack.getItem().isDamageable())
        break addCount;

      MutableText mutableText = new LiteralText("").append(list.get(0));
      list.set(0, new LiteralText(itemStack.getCount() + " " + mutableText.getString()));
    }

    narrateToolTip: {
      if (KeyboardController.hasControlOverMouse())
        break narrateToolTip;
      String message = "";

      for (Text text : list) {
        message += text.getString() + ", ";
      }

      if (!NarratorPlus.previousToolTip.equals(message)) {
        NarratorPlus.narrate(message);
        NarratorPlus.previousToolTip = message;
      }
    }

  }
}
