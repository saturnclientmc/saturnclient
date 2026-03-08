package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.CoordinatesInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class CoordinatesFabric implements CoordinatesInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public int getPlayerX() {
        Vec3d pos = mc.player.getPos();
        return (int) pos.x;
    }

    @Override
    public int getPlayerY() {
        Vec3d pos = mc.player.getPos();
        return (int) pos.y;
    }

    @Override
    public int getPlayerZ() {
        Vec3d pos = mc.player.getPos();
        return (int) pos.z;
    }
}