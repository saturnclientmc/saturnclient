package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.asset.SpriteRef;
import org.saturnclient.common.ref.game.EffectRef;
import org.saturnclient.saturnclient.SaturnClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectMixin implements EffectRef {

    @Shadow
    public abstract boolean shouldShowIcon();

    @Shadow
    public abstract boolean isInfinite();

    @Shadow
    public abstract int getDuration();

    @Shadow
    public abstract RegistryEntry<StatusEffect> getEffectType();

    @Override
    public SpriteRef getIcon() {
        SpriteIdentifier identifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
                InGameHud.getEffectTexture(getEffectType()));

        return (SpriteRef) SaturnClient.client.getAtlasManager().getSprite(identifier);
    }

    @Override
    public String getInfiniteText() {
        return "∞";
    }

    @Override
    public int getDurationSeconds() {
        return getDuration() / 20; // ticks → seconds
    }
}