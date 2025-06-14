package org.saturnclient.saturnclient.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui2.screens.ShiftMenu;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;

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

    private static final String FREELOOK_KEY_ID = "key.saturnclient.emote";
    private static final int FREELOOK_KEY = GLFW.GLFW_KEY_B;
    private static KeyBinding freelookKeyBinding;

    public static void register() {
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
                client.setScreen(new ShiftMenu());
            }

            if (SaturnClient.client.player != null && SaturnClient.client.player.isSneaking()) {
                AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(SaturnClient.client.player);
                animationStack.removeLayer(1000);
            }

            if (freelookKeyBinding.wasPressed()) {
                AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(SaturnClient.client.player);
                animationStack.addAnimLayer(1000, PlayerAnimationRegistry.getAnimation(Identifier.of("saturnclient", "backflip")).playAnimation());
            }
        });
    }
}
