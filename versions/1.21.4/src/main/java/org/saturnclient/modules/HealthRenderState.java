package org.saturnclient.modules;

import org.saturnclient.common.module.EntityModule.EntityType;

public interface HealthRenderState {
    float saturn$getHealth();

    float saturn$getMaxHealth();

    void saturn$setHealth(float health, float maxHealth);

    EntityType saturn$getEntityType();

    void saturn$setEntityType(EntityType type);
}
