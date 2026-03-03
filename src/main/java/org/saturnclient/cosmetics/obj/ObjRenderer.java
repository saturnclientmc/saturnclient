package org.saturnclient.cosmetics.obj;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.javagl.obj.*;

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
            return ObjReader.read(is);
        }
    }

    public static Map<String, Mtl> loadMtl(Identifier mtlId) throws IOException {
        try (InputStream is = MinecraftClient.getInstance().getResourceManager()
                .getResource(mtlId).get().getInputStream()) {
            List<Mtl> mtlList = MtlReader.read(is);
            Map<String, Mtl> mtlMap = new java.util.LinkedHashMap<>();
            for (Mtl mtl : mtlList) {
                mtlMap.put(mtl.getName(), mtl);
            }
            return mtlMap;
        }
    }

    public static void renderObj(Obj obj, Map<String, Mtl> mtlMap, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {

        MatrixStack.Entry entry = matrices.peek();

        Map<String, Obj> materialGroups = ObjSplitting.splitByMaterialGroups(obj);

        for (Map.Entry<String, Obj> group : materialGroups.entrySet()) {
            String materialName = group.getKey();
            Obj groupObj = group.getValue();

            Mtl mtl = mtlMap != null ? mtlMap.get(materialName) : null;

            // Resolve texture identifier using same logic as ObjUnbakedModelModel
            Identifier texture = Identifier.of("minecraft:textures/misc/white.png");

            if (mtl != null) {
                String mapKd = mtl.getMapKd();
                if (mapKd != null) {
                    // "#key" references are not resolvable at runtime without a texture map,
                    // so we only support direct resource location strings here
                    if (!mapKd.startsWith("#")) {
                        texture = Identifier.of(mapKd);
                    }
                }
            }

            // Diffuse color (Kd)
            float r = 1f, g = 1f, b = 1f;
            if (mtl != null) {
                FloatTuple kd = mtl.getKd();
                if (kd != null) {
                    r = kd.getX();
                    g = kd.getY();
                    b = kd.getZ();
                }
            }

            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(texture));

            int faces = groupObj.getNumFaces();
            final float fr = r, fg = g, fb = b;

            for (int f = 0; f < faces; f++) {
                ObjFace face = groupObj.getFace(f);
                int verts = face.getNumVertices();

                // Your original winding order logic, untouched
                for (int i = verts - 1; i >= 0; i--) {
                    writeVertex(groupObj, face, i, consumer, entry, light, overlay, fr, fg, fb);
                }
            }
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