package org.saturnclient.ui.resources;

import org.saturnclient.common.minecraft.SaturnIdentifier;

public class Textures {
    public static final SaturnIdentifier CROSSHAIR_RANGE = new SaturnIdentifier("saturnclient",
            "textures/gui/hud/crosshair_range.png");
    public static final SaturnIdentifier COSMETICS = new SaturnIdentifier("saturnclient",
            "textures/gui/components/cosmetics.png");
    public static final SaturnIdentifier HUD_ICON = new SaturnIdentifier("saturnclient",
            "textures/gui/components/hud.png");
    public static final SaturnIdentifier SEARCH = new SaturnIdentifier("saturnclient",
            "textures/gui/components/search.png");
    public static final SaturnIdentifier CLOSE = new SaturnIdentifier("saturnclient",
            "textures/gui/components/close.png");
    public static final SaturnIdentifier MODS_TAB = new SaturnIdentifier("saturnclient",
            "textures/gui/components/mods_tab.png");
    public static final SaturnIdentifier SETTINGS = new SaturnIdentifier("saturnclient",
            "textures/gui/components/settings.png");
    public static final SaturnIdentifier CLOAK = new SaturnIdentifier("saturnclient",
            "textures/gui/components/cloak.png");
    public static final SaturnIdentifier HAT = new SaturnIdentifier("saturnclient", "textures/gui/components/hat.png");
    public static final SaturnIdentifier RESET = new SaturnIdentifier("saturnclient",
            "textures/gui/components/reset.png");
    public static final SaturnIdentifier TOGGLE_INDICATOR = new SaturnIdentifier("saturnclient",
            "textures/gui/components/toggle_indicator.png");
    public static final SaturnIdentifier LEFT = new SaturnIdentifier("saturnclient",
            "textures/gui/components/left.png");
    public static final SaturnIdentifier RIGHT = new SaturnIdentifier("saturnclient",
            "textures/gui/components/right.png");
    public static final SaturnIdentifier STORE = new SaturnIdentifier("saturnclient",
            "textures/gui/components/store.png");
    public static final SaturnIdentifier COINS = new SaturnIdentifier("saturnclient",
            "textures/gui/components/coins.png");
    public static final SaturnIdentifier SHIRT = new SaturnIdentifier("saturnclient",
            "textures/gui/components/shirt.png");
    public static final SaturnIdentifier CIRCLE_X = new SaturnIdentifier("saturnclient",
            "textures/gui/components/circle_x.png");
    public static final SaturnIdentifier INFO = new SaturnIdentifier("saturnclient",
            "textures/gui/components/info.png");
    public static final SaturnIdentifier CHECK = new SaturnIdentifier("saturnclient",
            "textures/gui/components/check.png");

    public static final SaturnIdentifier LOGO = new SaturnIdentifier("saturnclient", "textures/logo/logo.png");
    public static final SaturnIdentifier REALISTIC_LOGO = new SaturnIdentifier("saturnclient",
            "textures/logo/realistic.png");
    public static final SaturnIdentifier LOGO_TEXT = new SaturnIdentifier("saturnclient", "textures/logo/text.png");
    public static final SaturnIdentifier LOGO_TEXT_BIG = new SaturnIdentifier("saturnclient",
            "textures/logo/text_big.png");
    public static final SaturnIdentifier SPLASH = new SaturnIdentifier("saturnclient", "textures/gui/splash.png");

    // Widgets
    public static final SaturnIdentifier BUTTON_BORDER = new SaturnIdentifier("saturnclient", "button_border");
    public static final SaturnIdentifier BUTTON = new SaturnIdentifier("saturnclient", "button");
    public static final SaturnIdentifier BUTTON_OP = new SaturnIdentifier("saturnclient", "button_op");
    public static final SaturnIdentifier SETTINGS_BG = new SaturnIdentifier("saturnclient", "settings_bg");
    public static final SaturnIdentifier RECT = new SaturnIdentifier("saturnclient", "rect");
    public static final SaturnIdentifier RECT_BORDER = new SaturnIdentifier("saturnclient", "rect_border");
    public static final SaturnIdentifier MOD = new SaturnIdentifier("saturnclient", "textures/gui/components/mod.png");
    public static final SaturnIdentifier MOD_BG = new SaturnIdentifier("saturnclient",
            "textures/gui/components/mod_bg.png");

    public static SaturnIdentifier getCloakPreview(String cloak) {
        return new SaturnIdentifier("saturnclient",
                "textures/gui/cloak/" + (cloak.isEmpty() ? "none" : cloak) + ".png");
    }

    public static SaturnIdentifier getHatPreview(String hat) {
        return new SaturnIdentifier("saturnclient", "textures/gui/hat/" + (hat.isEmpty() ? "none" : hat) + ".png");
    }

    public static SaturnIdentifier getEmotePreview(String emote) {
        return new SaturnIdentifier("saturnclient", "textures/gui/emote/" + (emote == null ? "none" : emote) + ".png");
    }

    public static SaturnIdentifier getModIcon(String modId) {
        return new SaturnIdentifier("saturnclient", "textures/gui/mod/" + modId + ".svg");
    }

}
