package org.saturnclient.modules.mixins;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import org.saturnclient.common.module.EntityModule;
import org.saturnclient.feature.features.NametagsFeature;
import org.saturnclient.modules.HealthRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class NametagsMixin {

    /**
     * Guards against infinite recursion: our inject calls
     * {@code renderLabelIfPresent} again with the replacement text, so we
     * use a thread-local flag to skip re-entry on that second call.
     */
    @Unique
    private static final ThreadLocal<Boolean> saturn$rendering = ThreadLocal.withInitial(() -> false);

    @Shadow
    protected abstract <S extends EntityRenderState> void renderLabelIfPresent(
            S state, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light);

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void saturn$replaceNametag(
            S state, Text text,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, CallbackInfo ci) {

        if (saturn$rendering.get())
            return;
        if (!NametagsFeature.shouldReplaceName())
            return;
        if (!(state instanceof LivingEntityRenderState liv))
            return;

        String replacement = NametagsFeature.getNametagString(
                new RenderStateEntityState(liv));
        if (replacement == null)
            return;

        ci.cancel();
        saturn$rendering.set(true);
        try {
            renderLabelIfPresent(state, Text.literal(replacement),
                    matrices, vertexConsumers, light);
        } finally {
            saturn$rendering.set(false);
        }
    }

    // ------------------------------------------------------------------
    // Inner adapter — bridges LivingEntityRenderState → EntityModule.EntityState
    // Kept here so the mixin package is self-contained; no separate Fabric
    // impl class is needed for this thin adapter.
    // ------------------------------------------------------------------

    @Unique
    private static final class RenderStateEntityState implements EntityModule.EntityState {

        private final LivingEntityRenderState state;
        private final HealthRenderState hrs;

        RenderStateEntityState(LivingEntityRenderState state) {
            this.state = state;
            // LivingEntityRenderStateMixin implements HealthRenderState
            this.hrs = (state instanceof HealthRenderState h) ? h : null;
        }

        @Override
        public String getCustomName() {
            // The vanilla render state stores the display name as a Text;
            // we expose it as a plain string for feature logic.
            return state.customName != null ? state.customName.getString() : null;
        }

        @Override
        public float getHealth() {
            return hrs != null ? hrs.saturn$getHealth() : 0f;
        }

        @Override
        public float getMaxHealth() {
            return hrs != null ? hrs.saturn$getMaxHealth() : 1f;
        }

        @Override
        public EntityModule.EntityType getEntityType() {
            return hrs != null ? hrs.saturn$getEntityType() : EntityModule.EntityType.OTHER;
        }
    }
}
