package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

import org.saturnclient.client.player.Roles;
import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.impl.cosmetics.CloakFeatureRenderer;
import org.saturnclient.impl.cosmetics.HatFeatureRenderer;
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

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    protected void renderLabelIfPresent(PlayerEntityRenderState state, MatrixStack matrices,
            OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (state.displayName == null)
            return;

        UUID uuid = isSaturn(state);

        if (uuid != null) {
            MutableText iconText = Text.literal(Roles.getSaturnIndicator())
                    .styled(style -> style.withColor(Roles.getIconColor(uuid)));

            Text nameText = state.displayName.copy().styled(style -> style.withColor(Formatting.WHITE));

            queue.submitLabel(matrices, state.nameLabelPos, 0, iconText.append(nameText), !state.sneaking, state.light,
                    state.squaredDistanceToCamera, cameraRenderState);

            ci.cancel();
        }
    }

    private UUID isSaturn(PlayerEntityRenderState state) {
        if (state instanceof PlayerEntityRenderState) {
            String name = ((PlayerEntityRenderState) state).displayName.getString();
            SaturnPlayer player = SaturnPlayer.get(name);
            if (player != null) {
                return player.uuid;
            }
        }

        return null;
    }
}