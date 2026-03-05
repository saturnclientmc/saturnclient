package org.saturnclient.modules.mods.utils;

public interface HealthRenderState {
    float saturn$getHealth();
    float saturn$getMaxHealth();
    void saturn$setHealth(float health, float maxHealth);

    EntityType saturn$getEntityType();
    void saturn$setEntityType(EntityType type);

    enum EntityType {
        PLAYER, HOSTILE, PASSIVE, OTHER
    }
}
