package org.saturnclient.impl.features.entity;

import org.saturnclient.common.feature.EntityFeature;

/**
 * Injected interface on
 * {@link net.minecraft.client.render.entity.state.LivingEntityRenderState}.
 *
 * Carries the entity's health snapshot and classification into the render
 * pipeline so that {@link NametagsMixin} can read them without touching
 * the live entity again.
 */
public interface HealthRenderState {

    float saturn$getHealth();

    float saturn$getMaxHealth();

    void saturn$setHealth(float health, float maxHealth);

    EntityFeature.EntityType saturn$getEntityType();

    void saturn$setEntityType(EntityFeature.EntityType type);
}
