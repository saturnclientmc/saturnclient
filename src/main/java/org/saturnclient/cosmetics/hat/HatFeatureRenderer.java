package org.saturnclient.cosmetics.hat;

import java.io.IOException;

import org.saturnclient.cosmetics.obj.ObjRenderer;

import de.javagl.obj.Obj;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HatFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    static Identifier HAT = Identifier.of("saturnclient", "models/item/horns/horns.obj");
    Obj obj;

    public HatFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
        try {
            this.obj = ObjRenderer.loadObj(HAT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        ObjRenderer.renderObj(this.obj, matrices, vertexConsumers, light, 0);
    }
}