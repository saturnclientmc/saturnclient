package org.saturnclient.ui2.resources;

import net.minecraft.util.Identifier;

public class Textures {
    public static final Identifier CROSSHAIR_RANGE = Identifier.of("saturnclient", "textures/gui/hud/crosshair_range.png");
    public static final Identifier COSMETICS = Identifier.of("saturnclient", "textures/gui/components/cosmetics.png");
    public static final Identifier HUD_ICON = Identifier.of("saturnclient", "textures/gui/components/hud.png");
    public static final Identifier SEARCH = Identifier.of("saturnclient", "textures/gui/components/search.png");
    public static final Identifier CLOSE = Identifier.of("saturnclient", "textures/gui/components/close.png");
    public static final Identifier MODS_TAB = Identifier.of("saturnclient", "textures/gui/components/mods_tab.png");
    public static final Identifier SETTINGS = Identifier.of("saturnclient", "textures/gui/components/settings.png");
    public static final Identifier CLOAK = Identifier.of("saturnclient", "textures/gui/components/cloak.png");
    public static final Identifier HAT = Identifier.of("saturnclient", "textures/gui/components/hat.png");
    public static final Identifier RESET = Identifier.of("saturnclient", "textures/gui/components/reset.png");
    public static final Identifier TOGGLE_INDICATOR = Identifier.of("saturnclient",
            "textures/gui/components/toggle_indicator.png");
    public static final Identifier LEFT = Identifier.of("saturnclient", "textures/gui/components/left.png");
    public static final Identifier RIGHT = Identifier.of("saturnclient", "textures/gui/components/right.png");
    public static final Identifier STORE = Identifier.of("saturnclient", "textures/gui/components/store.png");
    public static final Identifier COINS = Identifier.of("saturnclient", "textures/gui/components/coins.png");
    public static final Identifier SHIRT = Identifier.of("saturnclient", "textures/gui/components/shirt.png");
    public static final Identifier CIRCLE_X = Identifier.of("saturnclient", "textures/gui/components/circle_x.png");
    public static final Identifier INFO = Identifier.of("saturnclient", "textures/gui/components/info.png");
    public static final Identifier CHECK = Identifier.of("saturnclient", "textures/gui/components/check.png");

    public static final Identifier LOGO = Identifier.of("saturnclient", "textures/logo/logo.png");
    public static final Identifier REALISTIC_LOGO = Identifier.of("saturnclient", "textures/logo/realistic.png");
    public static final Identifier LOGO_TEXT = Identifier.of("saturnclient", "textures/logo/text.png");
    public static final Identifier LOGO_TEXT_BIG = Identifier.of("saturnclient", "textures/logo/text_big.png");

    // Widgets
    public static final Identifier BUTTON_BORDER = Identifier.of("saturnclient", "button_border");
    public static final Identifier BUTTON = Identifier.of("saturnclient", "button");
    public static final Identifier BUTTON_OP = Identifier.of("saturnclient", "button_op");
    public static final Identifier SETTINGS_BG = Identifier.of("saturnclient", "settings_bg");
    public static final Identifier RECT = Identifier.of("saturnclient", "rect");
    public static final Identifier RECT_BORDER = Identifier.of("saturnclient", "rect_border");
    public static final Identifier MOD = Identifier.of("saturnclient", "textures/gui/components/mod.png");
    public static final Identifier MOD_BG = Identifier.of("saturnclient", "textures/gui/components/mod_bg.png");

    public static Identifier getCloakPreview(String cloak) {
        return Identifier.of("saturnclient", "textures/gui/cloak/" + (cloak.isEmpty() ? "none" : cloak) + ".png");
    }

    public static Identifier getHatPreview(String hat) {
        return Identifier.of("saturnclient", "textures/gui/hat/" + (hat.isEmpty() ? "none" : hat) + ".png");
    }

    public static Identifier getEmotePreview(String emote) {
        return Identifier.of("saturnclient", "textures/gui/emote/" + (emote == null ? "none" : emote) + ".png");
    }

    public static Identifier getModIcon(String modId) {
        return Identifier.of("saturnclient", "textures/gui/mod/" + modId + ".png");
    }

}
