package org.saturnclient.saturnclient.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.saturnclient.saturnclient.menus.SaturnMenu;
import org.saturnclient.saturnmods.mods.FreeLook;
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

    private static final String FREELOOK_KEY_ID = "key.saturnclient.freelook";
    private static final int FREELOOK_KEY = GLFW.GLFW_KEY_M;
    private static KeyBinding freelookKeyBinding;

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

        freelookKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                FREELOOK_KEY_ID,
                InputUtil.Type.KEYSYM,
                FREELOOK_KEY,
                KEY_CATEGORY));

        // Register the event handler for the main menu key
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mainMenuKeyBinding.wasPressed()) {
                client.setScreen(new SaturnMenu());
            } else if (freelookKeyBinding.wasPressed() && FreeLook.enabled.value && FreeLook.toggle.value) {
                FreeLook.isFreeLooking = !FreeLook.isFreeLooking;
                if (FreeLook.isFreeLooking) {
                    FreeLook.lastPerspective = client.options.getPerspective();
                    client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                } else if (FreeLook.lastPerspective != null) {
                    client.options.setPerspective(FreeLook.lastPerspective);
                    FreeLook.lastPerspective = null;
                }
            } else if (FreeLook.enabled.value && !FreeLook.toggle.value) {
                if (freelookKeyBinding.isPressed()) {
                    if (!FreeLook.isFreeLooking) {
                        FreeLook.lastPerspective = client.options.getPerspective();
                        client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                        FreeLook.isFreeLooking = true;
                    }
                } else if (FreeLook.isFreeLooking && FreeLook.lastPerspective != null) {
                    client.options.setPerspective(FreeLook.lastPerspective);
                    FreeLook.isFreeLooking = false;
                    FreeLook.lastPerspective = null;
                }
            }
        });
    }
}
