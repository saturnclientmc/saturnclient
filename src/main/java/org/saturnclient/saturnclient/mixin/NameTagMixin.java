package org.saturnclient.saturnclient.mixin;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.config.SaturnClientConfig;

import java.util.UUID;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityRenderer.class, priority = 1800)
public abstract class NameTagMixin<S extends EntityRenderState> {
    @Shadow
    public EntityRenderDispatcher dispatcher;

    /**
     * @author HexLeo
     * @reason Adds the Saturn Client icon to the player's name if they are online
     *         with Saturn Client
     */
    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void onRenderLabelIfPresent(S state, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        UUID uuid = isSaturn(state);

        if (uuid != null) {
            MutableText iconText = Text.literal(SaturnClientConfig.getSaturnIndicator())
                    .styled(style -> style.withColor(SaturnClientConfig.getIconColor(uuid)));

            Text nameText = text.copy().styled(style -> style.withColor(Formatting.WHITE));

            renderLabel(state, iconText.append(nameText), matrices,
                    vertexConsumers, light);

            ci.cancel();
        }
    }

    private UUID isSaturn(S state) {
        if (state instanceof PlayerEntityRenderState) {
            String name = ((PlayerEntityRenderState) state).name;
            SaturnPlayer player = SaturnPlayer.get(name);
            return player.uuid;
        }

        return null;
    }

    private void renderLabel(S state, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        Vec3d vec3d = state.nameLabelPos;
        if (vec3d != null) {
            boolean bl = !state.sneaking;
            int i = "deadmau5".equals(text.getString()) ? -10 : 0;
            matrices.push();
            matrices.translate(vec3d.x, vec3d.y + 0.5, vec3d.z);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            TextRenderer textRenderer = SaturnClient.client.textRenderer;
            float f = (float) (-textRenderer.getWidth(text)) / 2.0F;
            int j = (int) (SaturnClient.client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
            textRenderer.draw(text, f, (float) i, -2130706433, false, matrix4f, vertexConsumers,
                    bl ? TextLayerType.SEE_THROUGH : TextLayerType.NORMAL, j, light);
            if (bl) {
                textRenderer.draw(text, f, (float) i, -1, false, matrix4f, vertexConsumers, TextLayerType.NORMAL, 0,
                        light);
            }
            matrices.pop();
        }
    }
}
