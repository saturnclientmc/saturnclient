package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.asset.SpriteRef;
import org.saturnclient.common.ref.game.EffectRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

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
        return null;
    }

    @Override
    public IdentifierRef getIconId() {
        Identifier id = InGameHud.getEffectTexture(getEffectType());

        return IdentifierRef.of(id.getNamespace(), "textures/" + id.getPath() + ".png");
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