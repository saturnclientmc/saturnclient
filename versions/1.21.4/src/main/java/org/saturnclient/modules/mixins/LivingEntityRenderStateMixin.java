package org.saturnclient.modules.mixins;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import org.saturnclient.modules.interfaces.NametagsInterface.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements org.saturnclient.modules.HealthRenderState {

    @Unique private float saturn$health = 0f;
    @Unique private float saturn$maxHealth = 1f;
    @Unique private EntityType saturn$entityType = EntityType.OTHER;

    public float saturn$getHealth() { return saturn$health; }
    public float saturn$getMaxHealth() { return saturn$maxHealth; }
    public void saturn$setHealth(float health, float maxHealth) {
        this.saturn$health = health;
        this.saturn$maxHealth = maxHealth;
    }

    public EntityType saturn$getEntityType() { return saturn$entityType; }
    public void saturn$setEntityType(EntityType type) { this.saturn$entityType = type; }

}