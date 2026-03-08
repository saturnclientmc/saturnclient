package org.saturnclient.modules.mixins;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import org.saturnclient.common.module.EntityModule;
import org.saturnclient.modules.HealthRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Adds health / entity-type fields to {@link LivingEntityRenderState} so
 * that {@link NametagsMixin} can read them without touching the live entity.
 *
 * The fields are populated each frame by {@link LivingEntityRendererMixin}.
 */
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements HealthRenderState {

    @Unique private float saturn$health    = 0f;
    @Unique private float saturn$maxHealth = 1f;
    @Unique private EntityModule.EntityType saturn$entityType = EntityModule.EntityType.OTHER;

    @Override public float saturn$getHealth()    { return saturn$health; }
    @Override public float saturn$getMaxHealth() { return saturn$maxHealth; }

    @Override
    public void saturn$setHealth(float health, float maxHealth) {
        this.saturn$health    = health;
        this.saturn$maxHealth = maxHealth;
    }

    @Override public EntityModule.EntityType saturn$getEntityType() { return saturn$entityType; }
    @Override public void saturn$setEntityType(EntityModule.EntityType type) { saturn$entityType = type; }
}
