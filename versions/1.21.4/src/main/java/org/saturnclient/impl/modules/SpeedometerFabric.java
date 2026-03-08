package org.saturnclient.impl.modules;

import org.saturnclient.feature.interfaces.SpeedometerInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class SpeedometerFabric implements SpeedometerInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public Velocity getVelocity() {
        Entity entity = mc.player.getVehicle() != null ? mc.player.getVehicle() : mc.player;
        return new Velocity(entity.getVelocity().x, entity.getVelocity().y, entity.getVelocity().z);
    }

    @Override
    public boolean isOnGround() {
        Entity entity = mc.player.getVehicle() != null ? mc.player.getVehicle() : mc.player;
        return entity.isOnGround();
    }
}