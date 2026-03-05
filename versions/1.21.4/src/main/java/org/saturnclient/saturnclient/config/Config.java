package org.saturnclient.saturnclient.config;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.config.manager.ConfigManager;
import org.saturnclient.saturnclient.config.manager.Property;
import org.saturnclient.ui2.resources.Textures;

import net.minecraft.util.Identifier;

public class Config {
    public static ConfigManager config;

    public static Property<Boolean> realisticLogo = Property.bool(false);
    public static Property<Boolean> saturnTitleScreen = Property.bool(true);
    public static Property<Boolean> cloakPhysics = Property.bool(true);
    public static Property<Integer> openEmoteWheel = Property.keybinding(GLFW.GLFW_KEY_B);
    public static Property<Boolean> stagger = Property.bool(true);

    public static Identifier getLogo() {
        return realisticLogo.value ? Textures.REALISTIC_LOGO : Textures.LOGO;
    }

    public static void init() {
        config = new ConfigManager("Saturn Client");
        config.property("Realistic logo", realisticLogo);
        config.property("Saturn client title screen", saturnTitleScreen);
        config.property("Open Emote Wheel", openEmoteWheel);
        config.property("Cloak Physics", cloakPhysics);
        config.property("Stagger Animations", stagger);

        // Initialize a sub namespace for theme
        Theme.init(config);

        // Initialize a sub namespace for animations
        AnimationConfig.init(config);
    }
}
