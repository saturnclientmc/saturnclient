package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import org.saturnclient.client.player.Roles;
import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.SaturnClient;

import java.util.UUID;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityRenderer.class, priority = 1800)
public abstract class NameTagMixin<T extends Entity> {
    @Shadow
    public EntityRenderDispatcher dispatcher;

    @Shadow
    private TextRenderer textRenderer;

    /**
     * @author HexLeo
     * @reason Adds the Saturn Client icon to the player's name if they are online
     *         with Saturn Client
     */
    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    protected void renderLabelIfPresent(T entity, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {

        UUID uuid = isSaturn(entity);

        if (uuid != null) {
            MutableText iconText = Text.literal(Roles.getSaturnIndicator())
                    .styled(style -> style.withColor(Roles.getIconColor(uuid)));

            Text nameText = text.copy().styled(style -> style.withColor(Formatting.WHITE));

            renderLabel(entity, iconText.append(nameText), matrices, vertexConsumers, light, tickDelta);

            ci.cancel();
        }
    }

    private UUID isSaturn(T entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            SaturnPlayer player = SaturnPlayer.get(playerEntity.getUuid());
            if (player != null) {
                return player.uuid;
            }
        }

        return null;
    }

    private void renderLabel(T entity, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, float tickDelta) {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (!(d > (double) 4096.0F)) {
            Vec3d vec3d = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0,
                    entity.getYaw(tickDelta));
            if (vec3d != null) {
                boolean bl = !entity.isSneaky();
                int i = "deadmau5".equals(text.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(vec3d.x, vec3d.y + (double) 0.5F, vec3d.z);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                float f = SaturnClient.client.options.getTextBackgroundOpacity(0.25F);
                int j = (int) (f * 255.0F) << 24;
                TextRenderer textRenderer = this.textRenderer;
                float g = (float) (-textRenderer.getWidth(text) / 2);
                textRenderer.draw(text, g, (float) i, 553648127, false, matrix4f, vertexConsumers,
                        bl ? TextLayerType.SEE_THROUGH : TextLayerType.NORMAL, j, light);
                if (bl) {
                    textRenderer.draw(text, g, (float) i, -1, false, matrix4f, vertexConsumers, TextLayerType.NORMAL, 0,
                            light);
                }

                matrices.pop();
            }
        }
    }
}
