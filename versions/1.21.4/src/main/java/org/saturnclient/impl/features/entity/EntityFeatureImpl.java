package org.saturnclient.impl.features.entity;

import org.saturnclient.common.feature.EntityFeature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.entity.LivingEntity;

/**
 * Fabric implementation of {@link EntityFeature}.
 *
 * Crosshair targeting is resolved via {@link MinecraftClient#crosshairTarget}.
 * Entity state for nametag rendering is supplied at render time by
 * {@link org.saturnclient.modules.mixins.LivingEntityRendererMixin} which
 * writes into the render state; {@link NametagsMixin} then constructs
 * a {@link RenderStateEntityState} from the already-extracted snapshot.
 */
public class EntityFeatureImpl implements EntityFeature {

    private final MinecraftClient mc;

    public EntityFeatureImpl(MinecraftClient mc) {
        this.mc = mc;
    }

    @Override
    public boolean isTargetingLivingEntity() {
        HitResult target = mc.crosshairTarget;
        if (target == null || target.getType() != HitResult.Type.ENTITY)
            return false;
        Entity hit = ((EntityHitResult) target).getEntity();
        return hit instanceof LivingEntity;
    }

    // ---------------------------------------------------------------
    // Render-state adapter
    // ---------------------------------------------------------------

    /**
     * Wraps a {@link HealthRenderState} (already injected into the
     * Minecraft render-state object) as a platform-neutral
     * {@link EntityState} for use by {@link NametagsFeature}.
     *
     * This is constructed inside {@link NametagsMixin} where both the
     * render-state mixin interface and the entity name are available.
     */
    public static final class RenderStateEntityState implements EntityState {

        private final String customName;
        private final HealthRenderState hrs;

        public RenderStateEntityState(String customName, HealthRenderState hrs) {
            this.customName = customName;
            this.hrs = hrs;
        }

        @Override
        public String getCustomName() {
            return customName;
        }

        @Override
        public float getHealth() {
            return hrs.saturn$getHealth();
        }

        @Override
        public float getMaxHealth() {
            return hrs.saturn$getMaxHealth();
        }

        @Override
        public EntityType getEntityType() {
            return hrs.saturn$getEntityType();
        }
    }
}
