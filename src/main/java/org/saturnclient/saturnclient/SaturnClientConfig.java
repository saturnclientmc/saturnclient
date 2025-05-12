package org.saturnclient.saturnclient;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.ui2.resources.Textures;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class SaturnClientConfig {
    public static ConfigManager config;

    public static Property<Boolean> realisticLogo =
            new Property<>(false);

    public static Property<Boolean> saturnTitleScreen = 
            new Property<>(true);

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
        config.property("Realistic logo", realisticLogo);
        config.property("Saturn client title screen", saturnTitleScreen);
    }
}
