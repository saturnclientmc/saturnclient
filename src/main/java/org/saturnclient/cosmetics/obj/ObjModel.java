package org.saturnclient.cosmetics.obj;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.javagl.obj.Mtl;
import de.javagl.obj.Obj;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ObjModel {
    public static final Map<Identifier, ObjModel> loadedObjModels = new HashMap<>();

    public record Config(
            @JsonProperty("transform") Transform transform) {

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public record Transform(double x, double y, double z) {
        }
    }

    Obj obj;
    Config config;

    public static ObjModel of(Identifier obj) {
        if (loadedObjModels.containsKey(obj))
            return loadedObjModels.get(obj);

        ObjModel o = new ObjModel(obj);
        loadedObjModels.put(obj, o);
        return o;
    }

    ObjModel(Identifier identifier) {
        Identifier obj = Identifier.of(identifier.getNamespace(), identifier.getPath() + ".obj");
        System.out.println(obj);
        // Identifier json = Identifier.of(identifier.getNamespace(), identifier.getPath() + ".json");

        try {
            this.obj = ObjRenderer.loadObj(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Identifier.of(obj.toString());
    }

    public void render(Map<String, Mtl> mtlMap, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            int overlay) {
        ObjRenderer.renderObj(this.obj, mtlMap, matrices, vertexConsumers, light, overlay);
    }
}
