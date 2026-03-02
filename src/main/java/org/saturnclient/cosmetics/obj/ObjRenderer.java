package org.saturnclient.cosmetics.obj;

import java.io.IOException;
import java.io.InputStream;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ObjRenderer {
    public static Obj loadObj(Identifier objId) throws IOException {
        try (InputStream is = MinecraftClient.getInstance().getResourceManager()
                .getResource(objId).get().getInputStream()) {
            return ObjUtils.convertToRenderable(ObjReader.read(is));
        }
    }

    public static void renderObj(Obj obj, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            int overlay) {
        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getEntityAlpha(Identifier.of("textures/misc/white.png")));

        MatrixStack.Entry entry = matrices.peek();

        int faces = obj.getNumFaces();

        for (int f = 0; f < faces; f++) {
            ObjFace face = obj.getFace(f);

            for (int i = 0; i < 3; i++) {
                writeVertex(obj, face, i, consumer, entry, light, overlay);
            }
        }
    }

    /** Write a single vertex to the VertexConsumer using the OBJ FloatTuple */
    private static void writeVertex(Obj obj, ObjFace face, int index, VertexConsumer consumer, MatrixStack.Entry matrix,
            int light, int overlay) {
        FloatTuple pos = obj.getVertex(face.getVertexIndex(index));
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();

        // Default normal
        float nx = 0f;
        float ny = 1f;
        float nz = 0f;

        // Use OBJ normal if available
        int normalIndex = face.getNormalIndex(index);
        if (normalIndex >= 0) {
            FloatTuple n = obj.getNormal(normalIndex);
            nx = n.getX();
            ny = n.getY();
            nz = n.getZ();
        }

        consumer.vertex(matrix, x, y, z)
                .color(255, 255, 255, 255)
                .texture(0f, 0f)
                .overlay(overlay)
                .light(light)
                .normal(matrix, nx, ny, nz);
    }
}