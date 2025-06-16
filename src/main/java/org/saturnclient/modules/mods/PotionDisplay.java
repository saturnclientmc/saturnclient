package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;

public class PotionDisplay extends Module implements HudMod {
    public static Property<Boolean> enabled = new Property<>(false);
    private static ModDimensions dimensions = new ModDimensions(40, 60);

    public PotionDisplay() {
        super(new ModuleDetails("Potion Display", "potions")
            .description("Displays potion effects")
            .version("v0.1.0")
            .tags("Utility"),
        
        enabled.named("Enabled"),
        dimensions.prop());
    }

    @Override
    public void renderHud(RenderScope scope) {
        int row = 0;

        StatusEffectSpriteManager statusEffectSpriteManager = SaturnClient.client.getStatusEffectSpriteManager();

        for (StatusEffectInstance effect : SaturnClient.client.player.getStatusEffects()) {
            if (effect.shouldShowIcon()) {
                RegistryEntry<StatusEffect> registryEntry = effect.getEffectType();
                Sprite sprite = statusEffectSpriteManager.getSprite(registryEntry);
                scope.drawSpriteStretched(sprite, 0, 18 * row, 16, 16);
                scope.drawText(0.6f, getDurationAsString(effect), 19, 18 * row + 2, dimensions.font.value, -1);
            }
        }
    }

    // This code was from: https://github.com/magicus/statuseffecttimer/blob/master/src/main/java/se/icus/mag/statuseffecttimer/mixin/StatusEffectTimerMixin.java
    private String getDurationAsString(StatusEffectInstance statusEffectInstance) {
		if (statusEffectInstance.isInfinite()) {
			return I18n.translate("effect.duration.infinite");
		}

		int ticks = MathHelper.floor((float) statusEffectInstance.getDuration());
		int totalSeconds = ticks / 20;

		if (totalSeconds >= 3600) {
			return totalSeconds / 3600 + "h";
		} else if (totalSeconds >= 60) {
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
			return String.format("%d:%02d", minutes, seconds);
		} else {
			return String.valueOf(totalSeconds) + "s";
		}
	}

    @Override
    public void renderDummy(RenderScope scope) {
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
