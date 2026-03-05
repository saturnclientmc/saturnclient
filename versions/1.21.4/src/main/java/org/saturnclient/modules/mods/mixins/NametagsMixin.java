package org.saturnclient.modules.mods.mixins;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.saturnclient.modules.mods.Nametags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class NametagsMixin {

    @Unique
    private static final ThreadLocal<Boolean> saturn$rendering = ThreadLocal.withInitial(() -> false);

    @Shadow
    protected abstract <S extends EntityRenderState> void renderLabelIfPresent(
        S state, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light
    );

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void saturn$replaceNametag(S state, Text text,
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, CallbackInfo ci) {

        if (saturn$rendering.get()) return;
        if (!Nametags.shouldReplaceName()) return;
        if (!(state instanceof LivingEntityRenderState living)) return;

        String replacement = Nametags.getNametagString(living);
        if (replacement == null) return;

        ci.cancel();
        saturn$rendering.set(true);
        try {
            renderLabelIfPresent(state, Text.literal(replacement), matrices, vertexConsumers, light);
        } finally {
            saturn$rendering.set(false);
        }
    }
}
