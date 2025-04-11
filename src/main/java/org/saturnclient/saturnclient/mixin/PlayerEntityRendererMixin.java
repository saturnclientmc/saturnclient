package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;

import org.saturnclient.saturnclient.cosmetics.CloakFeatureRenderer;
import org.saturnclient.saturnclient.cosmetics.HatFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
        extends LivingEntityRenderer<PlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {

    protected PlayerEntityRendererMixin() {
        super(null, null, 0.0f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomFeatureRenderer(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new HatFeatureRenderer(this));
        this.addFeature(new CloakFeatureRenderer(this, ctx.getEntityModels(), ctx.getEquipmentModelLoader()));
    }
}