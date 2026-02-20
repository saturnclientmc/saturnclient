package org.saturnclient.saturnclient.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.auth.Auth;
import org.saturnclient.ui2.screens.EmoteWheel;
import org.saturnclient.ui2.screens.ShiftMenu;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;

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

    public static void register() {
        mainMenuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                MAIN_MENU_KEY_ID,
                InputUtil.Type.KEYSYM,
                DEFAULT_MENU_KEY,
                KEY_CATEGORY));

        // Register the event handler for the main menu key
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mainMenuKeyBinding.wasPressed()) {
                client.setScreen(new ShiftMenu());
            }

            if (SaturnClientConfig.openEmoteWheel.wasKeyPressed() && client.currentScreen == null && !InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_F3)) {
                client.setScreen(new EmoteWheel());
            }

            if (SaturnClient.client.player != null && SaturnClient.client.player.isSneaking()) {
                AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(SaturnClient.client.player);
                if (animationStack.isActive() && animationStack.getPriority() == 1000) {
                    animationStack.removeLayer(1000);
                    Auth.emote("");
                }
            }
        });
    }
}
