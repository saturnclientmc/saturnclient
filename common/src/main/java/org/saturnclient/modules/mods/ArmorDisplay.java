package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.ArmorDisplayInterface;
import org.saturnclient.common.minecraft.bindings.SaturnItemStack;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.RenderScope;

public class ArmorDisplay extends Module implements HudMod {

    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(40, 75);
    private static Property<Boolean> useMainHand = Property.bool(true);

    private final ArmorDisplayInterface minecraft;

    public ArmorDisplay(ArmorDisplayInterface minecraft) {
        super(new ModuleDetails("Armor Display", "armor")
                .description("Displays armor durability")
                .version("v0.1.0")
                .tags("Utility"),
                enabled.named("Enabled"),
                useMainHand.named("Use Main Hand"),
                dimensions.prop());

        this.minecraft = minecraft;
    }

    public void renderArmor(RenderScope scope,
            SaturnItemStack mainHand,
            SaturnItemStack helmet,
            SaturnItemStack chestplate,
            SaturnItemStack leggings,
            SaturnItemStack boots) {

        int row = 0;

        if (useMainHand.value && !minecraft.isEmpty(mainHand)) {
            scope.drawItem(mainHand, 0, 15 * row);
            renderHealth(scope, row, minecraft.getMaxDamage(mainHand), minecraft.getDamage(mainHand));
            row++;
        }

        if (!minecraft.isEmpty(helmet)) {
            scope.drawItem(helmet, 0, 15 * row);
            renderHealth(scope, row, minecraft.getMaxDamage(helmet), minecraft.getDamage(helmet));
            row++;
        }

        if (!minecraft.isEmpty(chestplate)) {
            scope.drawItem(chestplate, 0, 15 * row);
            renderHealth(scope, row, minecraft.getMaxDamage(chestplate), minecraft.getDamage(chestplate));
            row++;
        }

        if (!minecraft.isEmpty(leggings)) {
            scope.drawItem(leggings, 0, 15 * row);
            renderHealth(scope, row, minecraft.getMaxDamage(leggings), minecraft.getDamage(leggings));
            row++;
        }

        if (!minecraft.isEmpty(boots)) {
            scope.drawItem(boots, 0, 15 * row);
            renderHealth(scope, row, minecraft.getMaxDamage(boots), minecraft.getDamage(boots));
            row++;
        }
    }

    public void renderHealth(RenderScope scope, int i, int maxDamage, int damage) {
        if (maxDamage > 0) {
            scope.drawText(
                    0.5f,
                    "" + (maxDamage - damage),
                    17,
                    (15 * i) + 3,
                    dimensions.font.value,
                    dimensions.fgColor.value);
        }
    }

    @Override
    public void renderHud(RenderScope scope) {
        renderArmor(scope,
                minecraft.getMainHand(),
                minecraft.getHelmet(),
                minecraft.getChestplate(),
                minecraft.getLeggings(),
                minecraft.getBoots());
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderArmor(scope,
                minecraft.getDummyMainHand(),
                minecraft.getDummyHelmet(),
                minecraft.getDummyChestplate(),
                minecraft.getDummyLeggings(),
                minecraft.getDummyBoots());
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
    public void onEnabled(boolean e) {
        enabled.value = e;
    }
}
