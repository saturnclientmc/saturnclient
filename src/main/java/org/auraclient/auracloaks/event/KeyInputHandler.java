package org.auraclient.auracloaks.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.auraclient.auracloaks.AuraCloaks;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_CAPES = "key.category.auracloaks";
    public static final String KEY_CHANGE_CAPE = "key.auracloaks.change_cape";

    public static KeyBinding changeCapeKey;

    public static void register() {
        changeCapeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_CHANGE_CAPE, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, KEY_CATEGORY_CAPES
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (changeCapeKey.wasPressed()) {
                if (AuraCloaks.list.indexOf(AuraCloaks.cape) + 1 == AuraCloaks.list.size()) {
                    AuraCloaks.cape = AuraCloaks.list.get(0);
                    AuraCloaks.capeCacheIdentifier = null;
                }else{
                    AuraCloaks.cape = AuraCloaks.list.get(AuraCloaks.list.indexOf(AuraCloaks.cape) + 1);
                };
            }
        });
    }
}

