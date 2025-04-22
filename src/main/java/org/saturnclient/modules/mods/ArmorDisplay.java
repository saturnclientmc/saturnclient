package org.saturnclient.modules.mods;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.saturnclient.config.Property;

public class ArmorDisplay extends Module implements HudMod {

    private static ConfigManager config = new ConfigManager("Armor Display");
    private static ModDimensions dimensions = new ModDimensions(
            config,
            0,
            0,
            34,
            75);

    public static Property<Integer> fgColor = config.property(
            "Foreground color",
            new Property<>(SaturnClientConfig.WHITE, Property.PropertyType.HEX));


    public ArmorDisplay() {
        super(config, "Armor Display", "armor");
    }

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
                    SaturnClientConfig.textRenderer,
                    "" + (item.getMaxDamage() - item.getDamage()),
                    0,
                    0,
                    fgColor.value,
                    false);

            matrices.pop();
        }

        return true;
    }

    @Override
    public void render(DrawContext context) {
        PlayerEntity player = SaturnClient.client.player;
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

    public ModDimensions getDimensions() {
        return dimensions;
    }
}
