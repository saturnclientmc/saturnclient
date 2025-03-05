package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.LightmapTextureManager;

import org.saturnclient.saturnclient.auth.SaturnApi;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class NameTagMixin<S extends EntityRenderState> {
    @Shadow
    public EntityRenderDispatcher dispatcher;

    @Overwrite
    public void renderLabelIfPresent(S state, Text text_o, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        Text text = isSaturn(state) ? Text.literal("" + "HexLeo") : text_o;

        Vec3d vec3d = state.nameLabelPos;
        if (vec3d != null) {
            boolean bl = !state.sneaking;
            int i = "deadmau5".equals(text.getString()) ? -10 : 0;
            matrices.push();
            matrices.translate(vec3d.x, vec3d.y + 0.5, vec3d.z);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            float f = (float) (-textRenderer.getWidth(text)) / 2.0F;
            int j = (int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
            textRenderer.draw(text, f, (float) i, -2130706433, false, matrix4f, vertexConsumers,
                    bl ? TextLayerType.SEE_THROUGH : TextLayerType.NORMAL, j, light);
            if (bl) {
                textRenderer.draw(text, f, (float) i, -1, false, matrix4f, vertexConsumers, TextLayerType.NORMAL, 0,
                        LightmapTextureManager.applyEmission(light, 2));
            }

            matrices.pop();
        }
    }

    private boolean isSaturn(S state) {
        if (state instanceof PlayerEntityRenderState) {
            String name = ((PlayerEntityRenderState) state).name;
            return SaturnApi.playerNames.containsKey(name);
        }
        return false;
    }
}
