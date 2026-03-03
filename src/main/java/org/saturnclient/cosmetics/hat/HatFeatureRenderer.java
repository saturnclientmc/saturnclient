package org.saturnclient.cosmetics.hat;

import java.io.IOException;
import java.util.Map;

import org.saturnclient.cosmetics.obj.ObjModel;
import org.saturnclient.cosmetics.obj.ObjRenderer;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;

import de.javagl.obj.Mtl;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HatFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    static Identifier BLACK_MTL = Identifier.of("saturnclient", "models/cosmetic/black.mtl");
    Map<String, Mtl> mtl;

    public HatFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
        try {
            this.mtl = ObjRenderer.loadMtl(BLACK_MTL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        ItemStack headItem = state.equippedHeadStack;
        SaturnPlayer player = SaturnPlayer.get(state.name);

        if (!headItem.isEmpty() || state.invisible || player == null || player.hat.isEmpty()) {
            return;
        }

        matrices.push();

        this.getContextModel().head.rotate(matrices);
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));

        matrices.scale(0.25f, 0.25f, 0.25f);
        matrices.translate(0.0f, 1.0f, 0.0f);
        ObjModel.of(Identifier.of("saturnclient", "models/cosmetic/" + player.hat.split("_")[0] + "/model")).render(
                this.mtl,
                matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);

        matrices.pop();
    }
}