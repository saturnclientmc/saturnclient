package org.saturnclient.ui2.components;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;

public class SkinPreview extends Element {
    private boolean negativeAngle = false;
    private float angle = 0.0f;

    public SkinPreview(float angle, boolean negativeAngle) {
        this.angle = angle;
        this.negativeAngle = negativeAngle;
    }

    public static void drawEntity(RenderScope renderScope, int x1, int y1, int x2, int y2, int size, float f, float mouseX,
            float mouseY, LivingEntity entity, float angle, boolean negativeAngle) {
        float g = (float) (x1 + x2) / 2.0F;
        float h = (float) (y1 + y2) / 2.0F;
        renderScope.enableScissor(0, 6, x2, y2);
        float i = (float) Math.atan((double) ((g - mouseX) / 40.0F));
        float j = (float) Math.atan((double) ((h - mouseY) / 40.0F));
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        float k = entity.bodyYaw;
        float l = entity.getYaw();
        float m = entity.getPitch();
        float n = entity.prevHeadYaw;
        float o = entity.headYaw;
        entity.setYaw(angle);
        entity.bodyYaw = angle;
        entity.setPitch(-(j * 20.0F));
        entity.headYaw = negativeAngle ? (i * 40.0F) + 180f : -(i * 40.0F);
        entity.prevHeadYaw = entity.getHeadYaw();
        entity.setCustomNameVisible(false);

        float p = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getHeight() / 2.0F + f * p, 0.0F);
        float q = (float) size / p;
        drawEntity(renderScope, g, h, q, vector3f, quaternionf, null, entity);

        entity.setCustomNameVisible(true);
        entity.bodyYaw = k;
        entity.setYaw(l);
        entity.setPitch(m);
        entity.prevHeadYaw = n;
        entity.headYaw = o;
        renderScope.disableScissor();
    }

    public static void drawEntity(RenderScope renderScope, float x, float y, float size, Vector3f vector3f,
            Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity) {
        renderScope.matrices.push();
        renderScope.matrices.translate((double) x, (double) y, 50.0);
        renderScope.matrices.scale(size, size, -size);
        renderScope.matrices.translate(vector3f.x, vector3f.y, vector3f.z);
        renderScope.matrices.multiply(quaternionf);
        renderScope.draw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = SaturnClient.client.getEntityRenderDispatcher();

        entityRenderDispatcher.setRenderShadows(false);
        renderScope.draw((vertexConsumers) -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 1.0F, renderScope.matrices, vertexConsumers,
                    15728880);
        });
        renderScope.draw();
        entityRenderDispatcher.setRenderShadows(true);
        renderScope.matrices.pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        LivingEntity entity = SaturnClient.client.player;
        if (entity != null) {
            drawEntity(renderScope, 0, 0, 75, 78, 30, 0.0625F, ctx.mouseX / scale, ctx.mouseY / scale, entity, angle, negativeAngle);
        }
    }
}
