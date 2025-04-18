package org.saturnclient.saturnclient;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.ui.SaturnAnimation;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.animations.FadeIn;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SaturnClientConfig {
    public static ConfigManager config = new ConfigManager("Saturn Client");

    public static Property<Integer> color = config.property(
            "Primary color",
            new Property<>(
                    ColorHelper.getArgb(255, 251, 60, 79),
                    Property.PropertyType.HEX));

    public static Property<Integer> normal = config.property(
            "Default color",
            new Property<>(
                    ColorHelper.getArgb(255, 182, 182, 182),
                    Property.PropertyType.HEX));

    public static Property<Boolean> realisticLogo = config.property(
            "Realistic logo",
            new Property<>(false));

    public static Property<Boolean> saturnTitleScreen = config.property(
            "Saturn client title screen",
            new Property<>(true));

    public static int WHITE = ColorHelper.getArgb(255, 255, 255, 255);
    public static TextRenderer textRenderer = null;

    public static int getWhite(float alpha) {
        return ((int) (alpha * 255) << 24) | (normal.value & 0x00FFFFFF);
    }

    public static int getColor(boolean hovering, float alpha) {
        return SaturnUi.getAlpha(hovering ? color.value : normal.value, alpha);
    }

    public static SaturnAnimation[] getAnimations() {
        return new SaturnAnimation[] { new FadeIn(1) };
    }

    public static Identifier getLogo() {
        return realisticLogo.value ? Textures.REALISTIC_LOGO : Textures.LOGO;
    }

    public static String getSaturnIndicator() {
        return realisticLogo.value ? "" : "";
    }
}
