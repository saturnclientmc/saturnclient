package org.saturnclient.impl.cosmetics.obj;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.impl.cosmetics.utils.ShaderUtils;

import de.javagl.obj.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ObjRenderer {

    public static Obj loadObj(Identifier objId) throws IOException {
        try (InputStream is = MinecraftClient.getInstance().getResourceManager()
                .getResource(objId).get().getInputStream()) {
            return ObjReader.read(is);
        }
    }

    public static void renderObj(Obj obj,
            Map<String, Mtl> mtlMap,
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            int overlay) {

        Map<String, Obj> materialGroups = ObjSplitting.splitByMaterialGroups(obj);

        for (Map.Entry<String, Obj> group : materialGroups.entrySet()) {
            String materialName = group.getKey();
            Obj groupObj = group.getValue();

            Mtl mtl = mtlMap != null ? mtlMap.get(materialName) : null;

            IdentifierRef texture = IdentifierRef.ofVanilla("textures/misc/white.png");

            if (mtl != null) {
                String mapKd = mtl.getMapKd();
                if (mapKd != null && !mapKd.startsWith("#")) {
                    texture = IdentifierRef.of(mapKd);
                }
            }

            float r = 1f, g = 1f, b = 1f;
            if (mtl != null) {
                FloatTuple kd = mtl.getKd();
                if (kd != null) {
                    r = kd.getX();
                    g = kd.getY();
                    b = kd.getZ();
                }
            }

            RenderLayer layer = ShaderUtils.getRenderLayer(texture);

            final float fr = r, fg = g, fb = b;
            final Obj finalObj = groupObj;

            queue.submitCustom(matrices, layer, (e, vertexConsumer) -> {

                int faces = finalObj.getNumFaces();

                for (int f = 0; f < faces; f++) {
                    ObjFace face = finalObj.getFace(f);
                    int verts = face.getNumVertices();

                    for (int i = verts - 1; i >= 0; i--) {
                        writeVertex(finalObj, face, i, vertexConsumer, e, light, overlay, fr, fg, fb);
                    }
                }
            });
        }
    }

    private static void writeVertex(Obj obj, ObjFace face, int index, VertexConsumer consumer,
            MatrixStack.Entry matrix, int light, int overlay,
            float r, float g, float b) {

        FloatTuple pos = obj.getVertex(face.getVertexIndex(index));
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();

        float nx = 0f, ny = 1f, nz = 0f;
        int normalIndex = face.getNormalIndex(index);
        if (normalIndex >= 0) {
            FloatTuple n = obj.getNormal(normalIndex);
            nx = n.getX();
            ny = n.getY();
            nz = n.getZ();
        }

        float u = 0f, v = 0f;
        int texIndex = face.getTexCoordIndex(index);
        if (texIndex >= 0) {
            FloatTuple tc = obj.getTexCoord(texIndex);
            u = tc.getX();
            v = 1f - tc.getY(); // Flip V: OBJ bottom-left origin -> Minecraft top-left
        }

        consumer.vertex(matrix, x, y, z)
                .color(r, g, b, 1f)
                .texture(u, v)
                .overlay(overlay)
                .light(light)
                .normal(matrix, nx, ny, nz);
    }
}