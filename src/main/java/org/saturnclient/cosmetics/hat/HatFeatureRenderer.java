package org.saturnclient.cosmetics.hat;

import java.io.IOException;
import java.util.Map;

import org.saturnclient.cosmetics.obj.ObjRenderer;

import de.javagl.obj.Mtl;
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
    static Identifier BLACK_MTL = Identifier.of("saturnclient", "models/item/black.mtl");
    Obj obj;
    Map<String, Mtl> mtl;

    public HatFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
        try {
            this.obj = ObjRenderer.loadObj(HAT);
            this.mtl = ObjRenderer.loadMtl(BLACK_MTL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        ObjRenderer.renderObj(this.obj, this.mtl, matrices, vertexConsumers, light, 0);
    }
}