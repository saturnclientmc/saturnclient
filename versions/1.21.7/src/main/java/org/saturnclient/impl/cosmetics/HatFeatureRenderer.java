package org.saturnclient.impl.cosmetics;

import org.joml.Quaternionf;
import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.impl.cosmetics.obj.MtlLoader;
import org.saturnclient.impl.cosmetics.obj.ObjModel;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.model.ModelPart;

public class HatFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    public HatFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        SaturnPlayer player = SaturnPlayer.get(state.name);

        if (state.invisible || player == null || player.hat.isEmpty()) {
            return;
        }

        matrices.push();

        ModelPart head = this.getContextModel().head;
        matrices.multiply(new Quaternionf().rotationZYX(head.roll, head.yaw, head.pitch));

        ObjModel.cosmetic("hat", player.hat).render(MtlLoader.cosmetic(player.hat),
                matrices, vertexConsumers, light,
                OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}