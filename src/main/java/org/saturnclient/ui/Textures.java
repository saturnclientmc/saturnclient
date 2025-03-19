package org.saturnclient.ui;

import net.minecraft.util.Identifier;

public class Textures {
    public static final Identifier COSMETICS = Identifier.of("saturnclient", "textures/gui/components/cosmetics.png");
    public static final Identifier HUD_ICON = Identifier.of("saturnclient", "textures/gui/components/hud.png");
    public static final Identifier SEARCH = Identifier.of("saturnclient", "textures/gui/components/search.png");
    public static final Identifier CLOSE = Identifier.of("saturnclient", "textures/gui/components/close.png");
    public static final Identifier MODS_TAB = Identifier.of("saturnclient", "textures/gui/components/mods_tab.png");
    public static final Identifier SETTINGS = Identifier.of("saturnclient",
            "textures/gui/components/settings.png");
    public static final Identifier LOGO = Identifier.of("saturnclient", "textures/logo/logo.png");
    public static final Identifier LOGO_TEXT = Identifier.of("saturnclient", "textures/logo/text.png");

    // Widgets
    public static final Identifier BUTTON_BORDER = Identifier.ofVanilla("saturn/button_border");
    public static final Identifier BUTTON = Identifier.ofVanilla("saturn/button");
    public static final Identifier SETTINGS_BG = Identifier.ofVanilla("saturn/settings_bg");
    public static final Identifier TABS = Identifier.ofVanilla("saturn/tabs");
    public static final Identifier MOD = Identifier.of("saturnclient", "textures/gui/components/mod.png");
    public static final Identifier MOD_BG = Identifier.of("saturnclient", "textures/gui/components/mod_bg.png");

    public static Identifier getCloakPreview(String cloak) {
        return Identifier.of("saturnclient", "textures/gui/cloak/" + cloak + ".png");
    }

    public static Identifier getModIcon(String modId) {
        return Identifier.of("saturnclient", "textures/gui/mod/" + modId + ".png");
    }
}
