package org.saturnclient.impl.ui;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.components.SkinPreview.EntityDrawer;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.LivingEntity;

public class EntityDrawerImpl implements EntityDrawer {
    @Override
    public void render(RenderScope renderScope, boolean negativeAngle, float angle, float scale, ElementContext ctx) {
        LivingEntity entity = SaturnClient.client.player;

        System.out.println(scale);

        if (entity != null) {
            drawEntity(renderScope, 0, 0, 75, 78, 30, 0.0625F, ctx.mouseX / scale, ctx.mouseY / scale,
                    SaturnClient.client.player);
        }
    }

    public static void drawEntity(RenderScope renderScope, int x1, int y1, int x2, int y2, int size, float scale,
            float mouseX, float mouseY, LivingEntity entity) {
        float f = (float) (x1 + x2) / 2.0F;
        float g = (float) (y1 + y2) / 2.0F;
        renderScope.enableScissor(x1, y1, x2, y2);
        float h = (float) Math.atan((double) ((f - mouseX) / 40.0F));
        float i = (float) Math.atan((double) ((g - mouseY) / 40.0F));
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(i * 20.0F * ((float) Math.PI / 180F));
        quaternionf.mul(quaternionf2);
        float j = entity.bodyYaw;
        float k = entity.getYaw();
        float l = entity.getPitch();
        float m = entity.lastHeadYaw;
        float n = entity.headYaw;
        entity.bodyYaw = 180.0F + h * 20.0F;
        entity.setYaw(180.0F + h * 40.0F);
        entity.setPitch(-i * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.lastHeadYaw = entity.getYaw();
        float o = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getHeight() / 2.0F + scale * o, 0.0F);
        float p = (float) size / o;
        drawEntity(renderScope, x1, y1, x2, y2, p, vector3f, quaternionf, quaternionf2, entity);
        entity.bodyYaw = j;
        entity.setYaw(k);
        entity.setPitch(l);
        entity.lastHeadYaw = m;
        entity.headYaw = n;
        renderScope.disableScissor();
    }

    public static void drawEntity(RenderScope renderScope, int x1, int y1, int x2, int y2, float scale,
            Vector3f translation, Quaternionf rotation, @Nullable Quaternionf overrideCameraAngle,
            LivingEntity entity) {
        EntityRenderDispatcher entityRenderDispatcher = SaturnClient.client.getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityRenderer = entityRenderDispatcher.getRenderer(entity);
        EntityRenderState entityRenderState = entityRenderer.getAndUpdateRenderState(entity, 1.0F);
        entityRenderState.hitbox = null;

        if (renderScope instanceof RenderScopeImpl i)
            i.addEntity(entityRenderState, scale, translation, rotation, overrideCameraAngle, x1, y1, x2, y2);
    }
}
