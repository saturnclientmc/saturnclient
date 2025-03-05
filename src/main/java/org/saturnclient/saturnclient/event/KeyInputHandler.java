package org.saturnclient.saturnclient.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.saturnclient.saturnclient.menus.MainMenu;
import org.lwjgl.glfw.GLFW;

/**
 * Handles keyboard input events for the Saturn Client.
 * Manages keybindings and their associated actions.
 */
public class KeyInputHandler {
    private static final String KEY_CATEGORY = "key.category.saturnclient";
    private static final String MAIN_MENU_KEY_ID = "key.saturnclient.main_menu";
    private static final int DEFAULT_MENU_KEY = GLFW.GLFW_KEY_RIGHT_SHIFT;

    private static KeyBinding mainMenuKeyBinding;

    /**
     * Registers all keybindings and their associated event handlers.
     * Should be called during mod initialization.
     */
    public static void register() {
        // Register the main menu key binding
        mainMenuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                MAIN_MENU_KEY_ID,
                InputUtil.Type.KEYSYM,
                DEFAULT_MENU_KEY,
                KEY_CATEGORY));

        // Register the event handler for the main menu key
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mainMenuKeyBinding.wasPressed()) {
                client.setScreen(new MainMenu());
            }
        });
    }
}
