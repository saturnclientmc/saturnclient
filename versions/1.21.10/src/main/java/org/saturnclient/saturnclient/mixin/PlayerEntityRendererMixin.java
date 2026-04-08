package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.network.ClientPlayerLikeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.saturnclient.client.player.Roles;
import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.impl.cosmetics.CloakFeatureRenderer;
import org.saturnclient.impl.cosmetics.HatFeatureRenderer;
import org.saturnclient.saturnclient.SaturnRenderState;
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
        this.addFeature(new CloakFeatureRenderer(this, ctx.getEquipmentModelLoader()));
    }

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    public <A extends PlayerLikeEntity & ClientPlayerLikeEntity> void updateRenderState(
            A entity, PlayerEntityRenderState state,
            float f, CallbackInfo ci) {
        if (entity == null) {
            return;
        }

        SaturnPlayer player = SaturnPlayer.get(entity.getUuid());

        state.setData(SaturnRenderState.saturnDataKey, player);

        if (player != null) {
            MutableText iconText = Text.literal(Roles.getSaturnIndicator())
                    .styled(style -> style.withColor(Roles.getIconColor(player.uuid)));

            state.displayName = Text.empty()
                    .append(iconText.copy())
                    .append(Text.empty().formatted(Formatting.RESET))
                    .append(state.displayName.copy());
        }
    }
}