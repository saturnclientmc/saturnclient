package org.saturnclient.modules.mods.mixins;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import org.saturnclient.modules.mods.utils.HealthRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements org.saturnclient.modules.mods.utils.HealthRenderState {

    @Unique private float saturn$health = 0f;
    @Unique private float saturn$maxHealth = 1f;
    @Unique private HealthRenderState.EntityType saturn$entityType = HealthRenderState.EntityType.OTHER;

    public float saturn$getHealth() { return saturn$health; }
    public float saturn$getMaxHealth() { return saturn$maxHealth; }
    public void saturn$setHealth(float health, float maxHealth) {
        this.saturn$health = health;
        this.saturn$maxHealth = maxHealth;
    }

    public HealthRenderState.EntityType saturn$getEntityType() { return saturn$entityType; }
    public void saturn$setEntityType(HealthRenderState.EntityType type) { this.saturn$entityType = type; }

}