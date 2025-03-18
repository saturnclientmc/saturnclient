package org.saturnclient.saturnmods.mods;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnmods.HudMod;
import org.saturnclient.saturnmods.ModDimensions;
import org.saturnclient.saturnmods.SaturnMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ArmorDisplay implements SaturnMod, HudMod {
    private static ModDimensions dimensions = new ModDimensions(120, 40);
    public static boolean enabled = false;

    private boolean renderItem(DrawContext context, ItemStack item, int y) {
        if (item.isEmpty()) {
            return false;
        }

        context.drawItem(item, 0, y);

        context.drawText(SaturnClient.textRenderer, "" + (item.getMaxDamage() - item.getDamage()),
                16, y + 4, SaturnClient.WHITE, false);

        return true;
    }

    @Override
    public void render(DrawContext context) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        ItemStack[] items = {
                player.getEquippedStack(EquipmentSlot.FEET),
                player.getEquippedStack(EquipmentSlot.LEGS),
                player.getEquippedStack(EquipmentSlot.CHEST),
                player.getEquippedStack(EquipmentSlot.HEAD),
                player.getEquippedStack(EquipmentSlot.MAINHAND),
        };

        int y = 75;

        for (ItemStack item : items) {
            if (renderItem(context, item, y)) {
                y -= 15;
            }
        }
    }

    @Override
    public void renderDummy(DrawContext context) {
        ItemStack[] items = {
                new ItemStack(Items.DIAMOND_BOOTS),
                new ItemStack(Items.DIAMOND_LEGGINGS),
                new ItemStack(Items.DIAMOND_CHESTPLATE),
                new ItemStack(Items.DIAMOND_HELMET),
                new ItemStack(Items.DIAMOND_SWORD),
        };

        int y = 75;

        for (ItemStack item : items) {
            if (renderItem(context, item, y)) {
                y -= 15;
            }
        }

        dimensions.width = 28;
        dimensions.height = 100;
    }

    public String getName() {
        return "Armor display";
    }

    public void setDimensions(ModDimensions d) {
        dimensions = d;
    }

    public ModDimensions getDimensions() {
        return dimensions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean e) {
        enabled = e;
    }
}
