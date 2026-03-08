package org.saturnclient.modules;

import org.saturnclient.common.module.EntityModule;

/**
 * Mixed into {@link net.minecraft.client.render.entity.state.LivingEntityRenderState}
 * by {@link org.saturnclient.modules.mixins.LivingEntityRenderStateMixin}.
 *
 * Carries the health snapshot and entity classification that
 * {@link org.saturnclient.modules.mixins.NametagsMixin} needs without
 * accessing the live entity during rendering.
 */
public interface HealthRenderState {

    float saturn$getHealth();
    float saturn$getMaxHealth();

    void saturn$setHealth(float health, float maxHealth);

    EntityModule.EntityType saturn$getEntityType();
    void saturn$setEntityType(EntityModule.EntityType type);
}
