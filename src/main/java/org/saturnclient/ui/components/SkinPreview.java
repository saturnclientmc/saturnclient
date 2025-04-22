package org.saturnclient.ui.components;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.saturnclient.ui.SaturnWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;

// If you think this code is bad, it's because it's written by Mojang

public class SkinPreview extends SaturnWidget {
    public static void drawEntity(DrawContext context, int x1, int y1, int x2, int y2, int size, float f, float mouseX,
            float mouseY, LivingEntity entity, float angle, boolean negativeAngle) {
        float g = (float) (x1 + x2) / 2.0F;
        float h = (float) (y1 + y2) / 2.0F;
        context.enableScissor(0, 6, x2, y2);
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
        entity.setPitch(-j * 20.0F);
        entity.headYaw = negativeAngle ? -i * 40.0F : i * 40.0F;
        entity.prevHeadYaw = entity.getHeadYaw();
        entity.setCustomNameVisible(false);

        float p = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getHeight() / 2.0F + f * p, 0.0F);
        float q = (float) size / p;
        drawEntity(context, g, h, q, vector3f, quaternionf, null, entity);

        entity.setCustomNameVisible(true);
        entity.bodyYaw = k;
        entity.setYaw(l);
        entity.setPitch(m);
        entity.prevHeadYaw = n;
        entity.headYaw = o;
        context.disableScissor();
    }

    public static void drawEntity(DrawContext context, float x, float y, float size, Vector3f vector3f,
            Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity) {
        context.getMatrices().push();
        context.getMatrices().translate((double) x, (double) y, 50.0);
        context.getMatrices().scale(size, size, -size);
        context.getMatrices().translate(vector3f.x, vector3f.y, vector3f.z);
        context.getMatrices().multiply(quaternionf);
        context.draw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = SaturnClient.client.getEntityRenderDispatcher();

        entityRenderDispatcher.setRenderShadows(false);
        context.draw((vertexConsumers) -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 1.0F, context.getMatrices(), vertexConsumers,
                    15728880);
        });
        context.draw();
        entityRenderDispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    @Override
    public void render(DrawContext context, boolean hovering, int mouseX, int mouseY) {
        LivingEntity entity = SaturnClient.client.player;
        if (entity != null) {
            drawEntity(context, 0, 0, 75, 78, 30, 0.0625F, mouseX, mouseY, entity, -30.0f, true);
        }
    }
}
