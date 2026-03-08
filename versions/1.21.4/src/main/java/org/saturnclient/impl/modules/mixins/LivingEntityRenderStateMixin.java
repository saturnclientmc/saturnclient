package org.saturnclient.impl.modules.mixins;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import org.saturnclient.common.module.EntityModule;
import org.saturnclient.impl.modules.HealthRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Implements {@link HealthRenderState} on
 * {@link LivingEntityRenderState}, injecting health and entity-type
 * storage into every living entity's render-state snapshot.
 *
 * Previously imported {@code NametagsInterface.EntityType}; the enum
 * now lives in {@link EntityModule.EntityType} which is the single
 * canonical definition shared by the mixin layer and the feature layer.
 *
 * The stored values are written by
 * {@link LivingEntityRendererMixin#saturn$captureHealth} each time a
 * render state is extracted from a live entity, and read by
 * {@link NametagsMixin} at render time.
 */
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements HealthRenderState {

    @Unique
    private float saturn$health = 0f;
    @Unique
    private float saturn$maxHealth = 1f;
    @Unique
    private EntityModule.EntityType saturn$entityType = EntityModule.EntityType.OTHER;

    @Override
    public float saturn$getHealth() {
        return saturn$health;
    }

    @Override
    public float saturn$getMaxHealth() {
        return saturn$maxHealth;
    }

    @Override
    public void saturn$setHealth(float health, float maxHealth) {
        this.saturn$health = health;
        this.saturn$maxHealth = maxHealth;
    }

    @Override
    public EntityModule.EntityType saturn$getEntityType() {
        return saturn$entityType;
    }

    @Override
    public void saturn$setEntityType(EntityModule.EntityType type) {
        saturn$entityType = type;
    }
}
