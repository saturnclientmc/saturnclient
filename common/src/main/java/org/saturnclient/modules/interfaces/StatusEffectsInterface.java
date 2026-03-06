package org.saturnclient.modules.interfaces;

import java.util.List;
import org.saturnclient.common.minecraft.bindings.SaturnSprite;

public interface StatusEffectsInterface {

    /** Returns a list of currently active effects */
    List<Effect> getActiveEffects();

    /** Returns a dummy list of effects for dummy rendering */
    List<Effect> getDummyEffects();

    /** Represents a single effect for rendering purposes */
    class Effect {
        private final SaturnSprite icon;
        private final int durationSeconds;
        private final boolean infinite;
        private final boolean showIcon;
        private final String infiniteText;

        public Effect(SaturnSprite icon, int durationSeconds, boolean infinite, boolean showIcon, String infiniteText) {
            this.icon = icon;
            this.durationSeconds = durationSeconds;
            this.infinite = infinite;
            this.showIcon = showIcon;
            this.infiniteText = infiniteText;
        }

        public SaturnSprite getIcon() { return icon; }
        public int getDurationSeconds() { return durationSeconds; }
        public boolean isInfinite() { return infinite; }
        public boolean shouldShowIcon() { return showIcon; }
        public String getInfiniteText() { return infiniteText; }
    }
}