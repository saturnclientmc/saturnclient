package org.auraclient.auraclient.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.auraclient.auraclient.menus.MainMenu;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_CAPES = "key.category.auraclient";
    public static final String KEY_MAIN_MENU = "key.auraclient.main_menu";
    public static KeyBinding mainMenuKey;

    public static void register() {
        mainMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_MAIN_MENU, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, KEY_CATEGORY_CAPES
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mainMenuKey.wasPressed()) {
                client.setScreen(new MainMenu());
            }
        });
    }
}

