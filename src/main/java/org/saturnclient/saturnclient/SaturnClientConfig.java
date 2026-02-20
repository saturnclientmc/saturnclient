package org.saturnclient.saturnclient;

import org.saturnclient.saturnclient.config.Property;

import java.util.Map;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.config.ConfigManager;
import org.saturnclient.ui2.resources.Textures;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class SaturnClientConfig {
    public static ConfigManager config;

    public static Property<Boolean> realisticLogo = Property.bool(false);
    public static Property<Boolean> saturnTitleScreen = Property.bool(true);
    public static Property<Boolean> bendyCloaks = Property.bool(true);
    public static Property<Integer> openEmoteWheel = Property.keybinding(GLFW.GLFW_KEY_B);

    static enum Role {
        OWNER,
        ADMIN,
        PARTNER,
        CONTRIBUTOR
    }

    private static final Map<UUID, Role> ROLES = Map.of(
            UUID.fromString("d362a042-28bd-49e1-a807-ae74dbe8aba9"),
            Role.OWNER);

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
    public static Formatting getIconColor(UUID uuid) {
        Role role = ROLES.get(uuid);

        if (role == null) {
            return Formatting.WHITE;
        }

        switch (role) {
            case OWNER:
                return Formatting.DARK_RED;

            case ADMIN:
                return Formatting.RED;

            case PARTNER:
                return Formatting.GOLD;

            case CONTRIBUTOR:
                return Formatting.AQUA;

            default:
                return Formatting.WHITE;
        }
    }

    public static void init() {
        config = new ConfigManager("Saturn Client");
        config.property("Realistic logo", realisticLogo);
        config.property("Saturn client title screen", saturnTitleScreen);
        config.property("Open Emote Wheel", openEmoteWheel);
        config.property("Bendy Cloaks", bendyCloaks);
    }
}
