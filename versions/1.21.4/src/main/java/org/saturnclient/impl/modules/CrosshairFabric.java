package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.CrosshairInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class CrosshairFabric implements CrosshairInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public boolean isTargetingLivingEntity() {

        Entity entity = mc.targetedEntity;

        return entity != null && entity.isAlive();
    }
}