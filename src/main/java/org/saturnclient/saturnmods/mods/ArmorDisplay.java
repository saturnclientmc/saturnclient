package org.saturnclient.saturnmods.mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnmods.HudMod;
import org.saturnclient.saturnmods.ModDimensions;
import org.saturnclient.saturnmods.SaturnMod;
import org.saturnclient.ui.Textures;

public class ArmorDisplay implements SaturnMod, HudMod {

    private static ConfigManager config = new ConfigManager("armor");
    public static Property<Boolean> enabled = config.property(
        "Enabled",
        new Property<>(false)
    );
    private static ModDimensions dimensions = new ModDimensions(
        config,
        0,
        0,
        34,
        75
    );

    public static Property<Integer> fgColor = config.property(
        "Foreground color",
        new Property<>(SaturnClient.WHITE, Property.PropertyType.HEX)
    );

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

            context.drawText(
                SaturnClient.textRenderer,
                "" + (item.getMaxDamage() - item.getDamage()),
                0,
                0,
                fgColor.value,
                false
            );

            matrices.pop();
        }

        return true;
    }

    @Override
    public void render(DrawContext context) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        ItemStack[] items = {
            player.getEquippedStack(EquipmentSlot.MAINHAND),
            player.getEquippedStack(EquipmentSlot.FEET),
            player.getEquippedStack(EquipmentSlot.LEGS),
            player.getEquippedStack(EquipmentSlot.CHEST),
            player.getEquippedStack(EquipmentSlot.HEAD),
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
            new ItemStack(Items.DIAMOND_SWORD),
            new ItemStack(Items.DIAMOND_BOOTS),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_HELMET),
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

    public ModDimensions getDimensions() {
        return dimensions;
    }

    public boolean isEnabled() {
        return enabled.value;
    }

    public void setEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public ConfigManager getConfig() {
        return config;
    }
}
