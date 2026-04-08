package org.saturnclient.impl.cosmetics;

import org.joml.Quaternionf;
import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.impl.cosmetics.obj.MtlLoader;
import org.saturnclient.impl.cosmetics.obj.ObjModel;
import org.saturnclient.saturnclient.SaturnRenderState;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
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
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, PlayerEntityRenderState state,
            float limbAngle, float limbDistance) {
        SaturnPlayer player = state.getData(SaturnRenderState.saturnDataKey);

        if (state.invisible || player == null || player.hat.isEmpty()) {
            return;
        }

        matrices.push();

        ModelPart head = this.getContextModel().head;
        matrices.multiply(new Quaternionf().rotationZYX(head.roll, head.yaw, head.pitch));

        ObjModel.cosmetic("hat", player.hat).render(MtlLoader.cosmetic(player.hat),
                matrices, queue, light,
                OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}