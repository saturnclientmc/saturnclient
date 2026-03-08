package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.AutoSprintInterface;

import net.minecraft.client.MinecraftClient;

public class AutoSprintFabric implements AutoSprintInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public boolean hasPlayer() {
        return mc.player != null;
    }

    @Override
    public boolean hasNetwork() {
        return mc.getNetworkHandler() != null;
    }

    @Override
    public boolean isForwardPressed() {
        return mc.options.forwardKey.isPressed();
    }

    @Override
    public boolean isBackPressed() {
        return mc.options.backKey.isPressed();
    }

    @Override
    public boolean isSneaking() {
        return mc.player.isSneaking();
    }

    @Override
    public boolean hasHorizontalCollision() {
        return mc.player.horizontalCollision;
    }

    @Override
    public boolean isUsingItem() {
        return mc.player.isUsingItem();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        mc.player.setSprinting(sprinting);
    }
}