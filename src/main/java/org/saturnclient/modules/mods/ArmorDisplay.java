package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ArmorDisplay extends Module implements HudMod {
    private static Property<Boolean> enabled = new Property<>(true);
    private static ModDimensions dimensions = new ModDimensions(40, 60);

    public ArmorDisplay() {
        super(new ModuleDetails("Armor Display", "armor").description("Displays armor durability")
            .version("0.1.0")
            .tags("utility"),
            enabled.named("Enabled"),
            dimensions.prop());
    }

    public void renderArmor(RenderScope scope, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        int row = 0;

        if (!helmet.isEmpty()) {
            scope.drawItem(helmet, 0, 0);
            renderHealth(scope, row, helmet.getMaxDamage(), helmet.getDamage());
            row++;
        }

        if (!chestplate.isEmpty()) {
            scope.drawItem(chestplate, 0, 15 * row);
            renderHealth(scope, row, chestplate.getMaxDamage(), chestplate.getDamage());
            row++;
        }
        if (!leggings.isEmpty()) {
            scope.drawItem(leggings, 0, 15 * row);
            renderHealth(scope, row, leggings.getMaxDamage(), leggings.getDamage());
            row++;
        }

        if (!boots.isEmpty()) {
            scope.drawItem(boots, 0, 15 * row);
            renderHealth(scope, row, boots.getMaxDamage(), boots.getDamage());
            row++;
        }
    }

    public void renderHealth(RenderScope scope, int i, int max, int current) {
        scope.drawText(0.5f, String.valueOf(((double) current / max) * 100) + "%", 17, (15 * i) + 3, false, dimensions.fgColor.value);
    }

    @Override
    public void renderHud(RenderScope scope) {
        renderArmor(scope,
            SaturnClient.client.player.getEquippedStack(EquipmentSlot.HEAD),
            SaturnClient.client.player.getEquippedStack(EquipmentSlot.CHEST),
            SaturnClient.client.player.getEquippedStack(EquipmentSlot.LEGS),
            SaturnClient.client.player.getEquippedStack(EquipmentSlot.FEET));
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderArmor(scope, new ItemStack(Items.DIAMOND_HELMET), new ItemStack(Items.DIAMOND_CHESTPLATE), new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_BOOTS));
    }

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void setEnabled(boolean e) {
        enabled.value = e;
    }
}
