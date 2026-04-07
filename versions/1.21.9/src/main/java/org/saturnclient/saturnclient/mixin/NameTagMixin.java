package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.saturnclient.client.player.Roles;
import org.saturnclient.client.player.SaturnPlayer;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityRenderer.class, priority = 1800)
public abstract class NameTagMixin<S extends EntityRenderState> {
    @Shadow
    public EntityRenderManager dispatcher;

    /**
     * @author HexLeo
     * @reason Adds the Saturn Client icon to the player's name if they are online
     *         with Saturn Client
     */
    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    protected void renderLabelIfPresent(S state, MatrixStack matrices, OrderedRenderCommandQueue queue,
            CameraRenderState cameraRenderState, CallbackInfo ci) {
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

    private UUID isSaturn(S state) {
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
