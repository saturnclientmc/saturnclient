package org.saturnclient.saturnclient;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.ui.SaturnAnimation;
import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.animations.FadeIn;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class SaturnClientConfig {
    public static ConfigManager config;

    public static Property<Integer> color =
            new Property<>(
                    ColorHelper.getArgb(255, 251, 60, 79),
                    Property.PropertyType.HEX);

    public static Property<Integer> normal =
            new Property<>(
                    ColorHelper.getArgb(255, 182, 182, 182),
                    Property.PropertyType.HEX);

    public static Property<Boolean> realisticLogo =
            new Property<>(false);

    public static Property<Boolean> saturnTitleScreen = 
            new Property<>(true);

    public static int WHITE = ColorHelper.getArgb(255, 255, 255, 255);

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

    /*
     * Gets the icon color of a specific individual, here are the different colors
     * 
     * - Owner: Dark Red
     * - Admin: Red
     * - Partners: Gold
     * - Contributor: Aqua
     * - Other/player: White
    */
    public static Formatting getIconColor(String uuid) {
        if (uuid.equals("d362a04228bd49e1a807ae74dbe8aba9")) {
            return Formatting.DARK_RED; // Owner
        }

        return Formatting.WHITE;
    }

    public static void init() {
        config = new ConfigManager("Saturn Client");

        config.property("Primary color", color);
        config.property("Default color", normal);
        config.property("Realistic logo", realisticLogo);
        config.property("Saturn client title screen", saturnTitleScreen);
    }
}
