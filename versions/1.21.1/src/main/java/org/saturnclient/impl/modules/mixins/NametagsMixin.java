package org.saturnclient.impl.modules.mixins;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
// import net.minecraft.client.render.entity.state.EntityRenderState;
// import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import org.saturnclient.feature.features.NametagsFeature;
import org.saturnclient.impl.modules.EntityModuleFabric;
import org.saturnclient.impl.modules.HealthRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts nametag rendering and replaces the displayed text with
 * the string produced by {@link NametagsFeature#getNametagString}.
 *
 * Key changes from the original:
 *
 * <ul>
 * <li>The old {@code NametagsFabric.EntityStateImpl} bridge class is
 * replaced by {@link EntityModuleFabric.RenderStateEntityState},
 * which wraps the {@link HealthRenderState} already injected into
 * the render state by {@link LivingEntityRenderStateMixin}.</li>
 * <li>The feature import changes from {@code Nametags} to
 * {@link NametagsFeature}; the two static methods
 * ({@code shouldReplaceName} / {@code getNametagString}) are
 * unchanged in signature.</li>
 * <li>The entity's display name is extracted from the render state's
 * {@code nameVisible} / text fields where available, falling back
 * to the plain text string passed to this method.</li>
 * </ul>
 *
 * The re-entrancy guard ({@code saturn$rendering}) is kept intact to
 * prevent infinite recursion when we call {@code renderLabelIfPresent}
 * with the replacement text.
 */
@Mixin(EntityRenderer.class)
public abstract class NametagsMixin {

    /** Guards against the recursive call we make with the replacement text. */
    @Unique
    private static final ThreadLocal<Boolean> saturn$rendering = ThreadLocal.withInitial(() -> false);

    // @Shadow
    // protected abstract <S extends EntityRenderState> void renderLabelIfPresent(
    //         S state, Text text,
    //         MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    // @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    // private <S extends EntityRenderState> void saturn$replaceNametag(
    //         S state, Text text,
    //         MatrixStack matrices, VertexConsumerProvider vertexConsumers,
    //         int light, CallbackInfo ci) {

    //     // Skip our own recursive call
    //     if (saturn$rendering.get())
    //         return;

    //     if (!NametagsFeature.shouldReplaceName())
    //         return;

    //     // Only living entities carry health data
    //     if (!(state instanceof LivingEntityRenderState living))
    //         return;
    //     if (!(living instanceof HealthRenderState hrs))
    //         return;

    //     // Build the platform-neutral EntityState from the render-state snapshot
    //     // (health/type were written by LivingEntityRendererMixin at extract time).
    //     String customName = text != null ? text.getString() : null;
    //     EntityModuleFabric.RenderStateEntityState entityState = new EntityModuleFabric.RenderStateEntityState(
    //             customName, hrs);

    //     // Ask the feature for the replacement string
    //     String replacement = NametagsFeature.getNametagString(entityState);
    //     if (replacement == null)
    //         return; // feature decided not to replace this entity

    //     ci.cancel();
    //     saturn$rendering.set(true);
    //     try {
    //         renderLabelIfPresent(state, Text.literal(replacement), matrices, vertexConsumers, light);
    //     } finally {
    //         saturn$rendering.set(false);
    //     }
    // }
}
