package org.saturnclient.saturnmods.mods;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnmods.HudMod;
import org.saturnclient.saturnmods.ModDimensions;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.Textures;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ArmorDisplay implements SaturnMod, HudMod {
    private static ConfigManager config = new ConfigManager("armor");
    private static ModDimensions dimensions = new ModDimensions(config, 0, 0, 34, 75);
    public static Property<Boolean> enabled = config.property("enabled", new Property<>(false));

    private boolean renderItem(DrawContext context, ItemStack item, int y) {
        if (item.isEmpty()) {
            return false;
        }

        context.drawItem(item, 0, y);

        if (item.getMaxDamage() > 0) {
            MatrixStack matrices = context.getMatrices();

            matrices.push();

            matrices.translate(17, y + 5, 0);

            matrices.scale(0.8f, 0.8f, 0f);

            context.drawText(SaturnClient.textRenderer, "" + (item.getMaxDamage() - item.getDamage()),
                    0, 0, SaturnClient.WHITE, false);

            matrices.pop();
        }

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

        int y = 60;

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

        int y = 60;

        for (ItemStack item : items) {
            if (renderItem(context, item, y)) {
                y -= 15;
            }
        }
    }

    public String getName() {
        return "Armor display";
    }

    @Override
    public Identifier getIconTexture() {
        return Textures.getModIcon("armor");
    }

    public void setDimensions(ModDimensions d) {
        dimensions = d;
    }

    public ModDimensions getDimensions() {
        return dimensions;
    }

    public boolean isEnabled() {
        return enabled.value;
    }

    public void setEnabled(boolean e) {
        enabled.value = e;
    }
}
