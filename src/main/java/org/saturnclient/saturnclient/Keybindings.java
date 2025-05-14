package org.saturnclient.saturnclient;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybindings {
    public static KeyBinding openGuiKey;

    public static void init() {
        openGuiKey = new KeyBinding("Open shift menu", Keyboard.KEY_RSHIFT, "Saturn Client");
        ClientRegistry.registerKeyBinding(openGuiKey);
    }
}
