package org.saturnclient.modules.mods.mixins;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements org.saturnclient.modules.mods.utils.HealthRenderState {

    @Unique private float saturn$health = 0f;
    @Unique private float saturn$maxHealth = 1f;

    @Override public float saturn$getHealth() { return saturn$health; }
    @Override public float saturn$getMaxHealth() { return saturn$maxHealth; }
    @Override public void saturn$setHealth(float health, float maxHealth) {
        this.saturn$health = health;
        this.saturn$maxHealth = maxHealth;
    }
}