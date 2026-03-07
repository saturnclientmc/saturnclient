package org.saturnclient.modules.mods;

import java.util.List;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.StatusEffectsInterface;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.RenderScope;

public class StatusEffects extends Module implements HudMod {

    public static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(60, 0);

    private final StatusEffectsInterface provider;

    public StatusEffects(StatusEffectsInterface provider) {
        super(new ModuleDetails("Status effects", "effect")
                .description("Displays status effects")
                .version("v0.1.0")
                .tags("Utility"),
                enabled.named("Enabled"),
                dimensions.prop());

        this.provider = provider;
    }

    private void renderEffects(RenderScope scope, List<StatusEffectsInterface.Effect> effects) {
        int row = 0;

        for (StatusEffectsInterface.Effect effect : effects) {
            if (!effect.shouldShowIcon()) continue;

            scope.drawSpriteStretched(effect.getIcon(), 0, 18 * row, 16, 16);
            scope.drawText(0.5f, getDurationAsString(effect), 18, 18 * row + 3, dimensions.font.value, -1);
            row++;
        }

        dimensions.height = 18 * row;
    }

    @Override
    public void renderHud(RenderScope scope) {
        renderEffects(scope, provider.getActiveEffects());
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderEffects(scope, provider.getDummyEffects());
    }

    private String getDurationAsString(StatusEffectsInterface.Effect effect) {
        if (effect.isInfinite()) {
            return effect.getInfiniteText();
        }

        int totalSeconds = effect.getDurationSeconds();
        if (totalSeconds >= 3600) {
            return totalSeconds / 3600 + "h";
        } else if (totalSeconds >= 60) {
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            return String.format("%d:%02d", minutes, seconds);
        } else {
            return totalSeconds + "s";
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
    public void onEnabled(boolean e) {
        enabled.value = e;
    }
}