package org.saturnclient.impl.cosmetics;

import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.impl.cosmetics.obj.MtlLoader;
import org.saturnclient.impl.cosmetics.obj.ObjModel;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class HatFeatureRenderer
        extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public HatFeatureRenderer(
            FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta,
            float animationProgress, float headYaw, float headPitch) {
        SaturnPlayer player = SaturnPlayer.get(entity.getUuid());

        if (entity.isInvisible() || player == null || player.hat.isEmpty()) {
            return;
        }

        matrices.push();

        this.getContextModel().head.rotate(matrices);

        ObjModel.cosmetic("hat", player.hat).render(MtlLoader.cosmetic(player.hat),
                matrices, vertexConsumers, light,
                OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}