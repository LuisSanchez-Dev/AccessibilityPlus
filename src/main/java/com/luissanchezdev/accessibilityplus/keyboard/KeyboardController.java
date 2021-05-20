package com.luissanchezdev.accessibilityplus.keyboard;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.util.List;

import com.luissanchezdev.accessibilityplus.NarratorPlus;
import com.luissanchezdev.accessibilityplus.config.Config;
import com.luissanchezdev.accessibilityplus.mixin.AccessorHandledScreen;

import org.lwjgl.glfw.GLFW;

import blue.endless.jankson.annotation.Nullable;
import me.shedaniel.cloth.api.client.events.v0.ClothClientHooks;
import me.shedaniel.cloth.api.client.events.v0.ScreenHooks;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.minecraft.item.ItemStack;

import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;

public class KeyboardController {
    private static KeyBinding LEFT_KEY;
    private static KeyBinding RIGHT_KEY;
    private static KeyBinding UP_KEY;
    private static KeyBinding DOWN_KEY;
    private static KeyBinding GROUP_KEY;
    private static KeyBinding HOME_KEY;
    private static KeyBinding END_KEY;
    private static KeyBinding CLICK_KEY;
    private static KeyBinding RIGHT_CLICK_KEY;
    private static MinecraftClient client;
    @Nullable
    private static List<SlotsGroup> groups;
    @Nullable
    private static SlotsGroup currentGroup;
    @Nullable
    private static Slot currentSlot;
    @Nullable
    private static AccessorHandledScreen screen;

    private static boolean narrateCursorStack = false;
    private static double lastMouseX = 0;
    private static double lastMouseY = 0;
    private static boolean hasControlOverMouse = false;

    private enum FocusDirection {
        UP, DOWN, LEFT, RIGHT
    }

    public KeyboardController() {
        LEFT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.left",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, "key.categories.accessibilityplus.inventorycontrol"));
        RIGHT_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.right",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT, "key.categories.accessibilityplus.inventorycontrol"));
        UP_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.up", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UP, "key.categories.accessibilityplus.inventorycontrol"));
        DOWN_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.down",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, "key.categories.accessibilityplus.inventorycontrol"));
        GROUP_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.group",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_TAB, "key.categories.accessibilityplus.inventorycontrol"));
        HOME_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.home",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_HOME, "key.categories.accessibilityplus.inventorycontrol"));
        END_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.end", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_END, "key.categories.accessibilityplus.inventorycontrol"));
        CLICK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.mouseclick",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.categories.accessibilityplus.inventorycontrol"));
        RIGHT_CLICK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.mouserightclick",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "key.categories.accessibilityplus.inventorycontrol"));

        ClientTickEvents.END_CLIENT_TICK.register(KeyboardController::onClientTick);
        ClothClientHooks.SCREEN_INIT_POST.register(KeyboardController::onScreenOpen);
        ClothClientHooks.SCREEN_KEY_PRESSED.register(KeyboardController::onKeyPress);
    }

    public static boolean hasControlOverMouse() {
        if (screen == null) {
            return false;
        } else {
            return hasControlOverMouse;
        }
    }

    private static ActionResult onScreenOpen(MinecraftClient mc, Screen currentScreen, ScreenHooks screenHooks) {
        client = mc;
        groups = null;
        screen = null;
        currentGroup = null;
        currentSlot = null;

        if (currentScreen != null && currentScreen instanceof AccessorHandledScreen) {
            screen = (AccessorHandledScreen) currentScreen;
            groups = SlotsGroup.generateGroupsFromSlots(screen.getHandler().slots);
            moveMouseToHome();
        }
        return ActionResult.PASS;
    }

    private static ActionResult onKeyPress(MinecraftClient mc, Screen currentScreen, int keyCode, int scanCode,
            int modifiers) {
        if (screen != null && Config.inventoryKeyboardControlEnabled()) {
            if (LEFT_KEY.matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.LEFT);
            } else if (RIGHT_KEY.matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.RIGHT);
            } else if (UP_KEY.matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.UP);
            } else if (DOWN_KEY.matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.DOWN);
            } else if (GROUP_KEY.matchesKey(keyCode, scanCode)) {
                if (modifiers == GLFW.GLFW_MOD_SHIFT) {
                    focusGroupVertically(false);
                } else {
                    focusGroupVertically(true);
                }
                return ActionResult.SUCCESS;
            } else if (HOME_KEY.matchesKey(keyCode, scanCode)) {
                if (modifiers == GLFW.GLFW_MOD_SHIFT) {
                    focusEdgeGroup(false);
                } else {
                    focusEdgeSlot(false);
                }
            } else if (END_KEY.matchesKey(keyCode, scanCode)) {
                if (modifiers == GLFW.GLFW_MOD_SHIFT) {
                    focusEdgeGroup(true);
                } else {
                    focusEdgeSlot(true);
                }
            } else if (CLICK_KEY.matchesKey(keyCode, scanCode)) {
                click(false);
            } else if (RIGHT_CLICK_KEY.matchesKey(keyCode, scanCode)) {
                click(true);
            }
        }
        return ActionResult.PASS;
    }

    private static void onClientTick(MinecraftClient mc) {
        if (screen == null) {
            return;
        }
        PointerInfo info = MouseInfo.getPointerInfo();
        double mouseX = info.getLocation().getX();
        double mouseY = info.getLocation().getY();
        hasControlOverMouse = mouseX == lastMouseX && mouseY == lastMouseY;

        if (narrateCursorStack) {
            narrateCursorStack = false;
            ItemStack cursorStack = client.player.inventory.getCursorStack();
            if (!cursorStack.isEmpty()) {

                /* Previous method of getting the item count
                   New method in ItemStackTooltipInject.java in /mixin
                String amount = String.valueOf(cursorStack.getCount());
                */
            	
                String message = "On cursor: ";
                for (Text line : cursorStack.getTooltip(client.player, TooltipContext.Default.NORMAL)) {
                    message += line.getString() + ", ";
                }
                NarratorPlus.narrate(message);
            } else {
                if (currentSlot != null) {
                    focusSlot(currentSlot);
                }
            }
        }
    }

    private static void focusSlotAt(FocusDirection direction) {
        if (currentGroup == null) {
            focusGroupVertically(true);
            return;
        }
        if (currentSlot == null) {
            focusSlot(currentGroup.getFirstSlot());
            return;
        }
        int targetDeltaX = 0;
        int targetDeltaY = 0;
        switch (direction) {
            case UP:
                if (!currentGroup.hasSlotAbove(currentSlot)) {
                    NarratorPlus.narrate("No more slots above");
                    return;
                }
                targetDeltaY = -18;
                break;
            case DOWN:
                if (!currentGroup.hasSlotBelow(currentSlot)) {
                    NarratorPlus.narrate("No more slots below");
                    return;
                }
                targetDeltaY = 18;
                break;
            case LEFT:
                if (!currentGroup.hasSlotLeft(currentSlot)) {
                    NarratorPlus.narrate("No more slots to the left");
                    return;
                }
                targetDeltaX = -18;
                break;
            case RIGHT:
                if (!currentGroup.hasSlotRight(currentSlot)) {
                    NarratorPlus.narrate("No more slots to the right");
                    return;
                }
                targetDeltaX = 18;
                break;
        }
        int targetX = currentSlot.x + targetDeltaX;
        int targetY = currentSlot.y + targetDeltaY;
        for (Slot s : currentGroup.slots) {
            if (s.x == targetX && s.y == targetY) {
                focusSlot(s);
                break;
            }
        }
    }

    private static void focusSlot(Slot slot) {
        currentSlot = slot;
        moveToSlot(currentSlot);
        String message = "";
        if (currentGroup.getSlotName(currentSlot).length() > 0) {
            message += currentGroup.getSlotName(currentSlot) + ". ";
        }
        if (!currentSlot.hasStack()) {
            message += " Empty";
        } else {

            /* Previous method of getting the item count
               New method in ItemStackTooltipInject.java in /mixin
            message += String.valueOf(currentSlot.getStack().getCount()) + " ";
            */
            List<Text> lines = currentSlot.getStack().getTooltip(client.player, TooltipContext.Default.NORMAL);
            for (Text line : lines) {
                message += line.getString() + ", ";
            }
        }
        if (message != null && message.length() > 0) {
            NarratorPlus.narrate(message);
        }
    }

    private static void focusEdgeSlot(boolean end) {
        if (currentGroup == null) {
            focusGroupVertically(true);
            return;
        }
        if (currentGroup.slots.size() == 1 && currentSlot != null) {
            NarratorPlus.narrate("This group only has one slot!");
            return;
        }
        focusSlot(end ? currentGroup.getLastSlot() : currentGroup.getFirstSlot());
    }

    private static void focusEdgeGroup(boolean last) {
        focusGroup(groups.get(last ? groups.size() - 1 : 0));
    }

    private static void focusGroupVertically(boolean goBelow) {
        if (currentGroup == null) {
            focusGroup(groups.get(0));
        } else {
            int currentGroupIndex = groups.indexOf(currentGroup);
            int nextGroupIndex = currentGroupIndex + (goBelow ? 1 : -1);
            if (nextGroupIndex < 0) {
                NarratorPlus.narrate("Reached the top group");
                return;
            } else if (nextGroupIndex > groups.size() - 1) {
                NarratorPlus.narrate("Reached the bottom group");
                return;
            } else {
                focusGroup(groups.get(nextGroupIndex));
                ;
            }
        }
    }

    private static void focusGroup(SlotsGroup group) {
        currentGroup = group;
        currentSlot = null;
        moveMouseToHome();
        NarratorPlus.narrate(currentGroup.name);
    }

    private static void moveMouseToHome() {
        SlotsGroup lastGroup = groups.get(groups.size() - 1);
        Slot lastSlot = lastGroup.getLastSlot();
        moveMouseToScreenCoords(lastSlot.x + 19, lastSlot.y + 19);
    }

    private static void click(boolean rightClick) {
        double scale = client.getWindow().getScaleFactor();
        double x = client.mouse.getX() / scale;
        double y = client.mouse.getY() / scale;
        int button = rightClick ? GLFW.GLFW_MOUSE_BUTTON_RIGHT : GLFW.GLFW_MOUSE_BUTTON_LEFT;
        client.currentScreen.mouseClicked(x, y, button);
        client.currentScreen.mouseReleased(x, y, button);
        narrateCursorStack = true;
    }

    private static void moveMouseTo(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            lastMouseX = (double) x;
            lastMouseY = (double) y;
        } catch (AWTException e) {
            e.printStackTrace();
        } finally {
        }
    }

    private static void moveMouseToScreenCoords(int x, int y) {
        double targetX = (screen.getX() + x) * client.getWindow().getScaleFactor() + client.getWindow().getX();
        double targetY = (screen.getY() + y) * client.getWindow().getScaleFactor() + client.getWindow().getY();
        moveMouseTo((int) targetX, (int) targetY);
    }

    private static void moveToSlot(Slot slot) {
        if (slot == null) {
            return;
        }
        moveMouseToScreenCoords(slot.x + 9, slot.y + 9);
    }
}
