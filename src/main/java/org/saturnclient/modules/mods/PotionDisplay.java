package org.saturnclient.modules.mods;

import java.util.Arrays;

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
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class PotionDisplay extends Module implements HudMod {
    public static Property<Boolean> enabled = new Property<>(false);
    private static ModDimensions dimensions = new ModDimensions(60, 0);
    private final StatusEffectInstance[] dummyEffects = {
        new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("speed")).get(), 12000, 2),
        new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("strength")).get(), 12000, 2),
        new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("fire_resistance")).get(), 12000, 2),
    };

    public PotionDisplay() {
        super(new ModuleDetails("Potion Display", "potion")
            .description("Displays potion effects")
            .version("v0.1.0")
            .tags("Utility"),
        
        enabled.named("Enabled"),
        dimensions.prop());
    }

    private void renderEffects(RenderScope scope, Iterable<StatusEffectInstance> effects) {
        int row = 0;

        StatusEffectSpriteManager statusEffectSpriteManager = SaturnClient.client.getStatusEffectSpriteManager();

        for (StatusEffectInstance effect : effects) {
            if (effect.shouldShowIcon()) {
                RegistryEntry<StatusEffect> registryEntry = effect.getEffectType();
                Sprite sprite = statusEffectSpriteManager.getSprite(registryEntry);
                scope.drawSpriteStretched(sprite, 0, 18 * row, 16, 16);
                scope.drawText(0.5f, getDurationAsString(effect), 20, 18 * row + 3, dimensions.font.value, -1);
                row++;
            }
        }

        dimensions.height = 18 * row;
    }

    @Override
    public void renderHud(RenderScope scope) {
        renderEffects(scope, SaturnClient.client.player.getStatusEffects());
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderEffects(scope, Arrays.asList(dummyEffects));
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
