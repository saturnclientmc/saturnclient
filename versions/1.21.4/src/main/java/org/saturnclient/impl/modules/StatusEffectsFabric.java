package org.saturnclient.impl.modules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.saturnclient.common.ref.asset.SpriteRef;
import org.saturnclient.modules.interfaces.StatusEffectsInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffectInstance;

public class StatusEffectsFabric implements StatusEffectsInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public List<Effect> getActiveEffects() {
        StatusEffectSpriteManager manager = mc.getStatusEffectSpriteManager();
        return mc.player.getStatusEffects().stream()
                .filter(StatusEffectInstance::shouldShowIcon)
                .map(se -> new Effect(
                        (SpriteRef) manager.getSprite(se.getEffectType()),
                        se.getDuration() / 20,
                        se.isInfinite(),
                        se.shouldShowIcon(),
                        "Infinite"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Effect> getDummyEffects() {
        return Arrays.asList(
                new Effect(null, 600, false, true, "Infinite"),
                new Effect(null, 600, false, true, "Infinite"));
    }
}