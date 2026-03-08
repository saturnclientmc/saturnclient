package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.KeystrokesInterface;

import net.minecraft.client.MinecraftClient;

public class KeystrokesFabric implements KeystrokesInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public boolean isForwardPressed() {
        return mc.options.forwardKey.isPressed();
    }

    @Override
    public boolean isBackPressed() {
        return mc.options.backKey.isPressed();
    }

    @Override
    public boolean isLeftPressed() {
        return mc.options.leftKey.isPressed();
    }

    @Override
    public boolean isRightPressed() {
        return mc.options.rightKey.isPressed();
    }

    @Override
    public boolean isJumpPressed() {
        return mc.options.jumpKey.isPressed();
    }

    @Override
    public boolean isAttackPressed() {
        return mc.options.attackKey.isPressed();
    }

    @Override
    public boolean isUsePressed() {
        return mc.options.useKey.isPressed();
    }
}