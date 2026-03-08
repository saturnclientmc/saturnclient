package org.saturnclient.impl.cosmetics.obj;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.javagl.obj.Mtl;
import de.javagl.obj.Obj;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class ObjModel {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final Map<Identifier, ObjModel> loadedObjModels = new HashMap<>();

    public record Config(
            @JsonProperty("translate") Vec3d transform,
            @JsonProperty("scale") Vec3d scale,
            @JsonProperty("rotation") Vec3d rotation) {

        public Config {
            transform = transform == null ? new Vec3d(0, 0, 0) : transform;
            scale = scale == null ? new Vec3d(1, 1, 1) : scale;
            rotation = rotation == null ? new Vec3d(0, 0, 0) : rotation;
        }

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public record Vec3d(float x, float y, float z) {
        }
    }

    Obj obj;
    Config config;

    public static ObjModel cosmetic(String cosmeticKind, String id) {
        return of(Identifier.of("saturnclient", "models/cosmetic/" + cosmeticKind + "/" + id.split("_")[0] + "/model"));
    }

    public static ObjModel of(Identifier obj) {
        if (loadedObjModels.containsKey(obj))
            return loadedObjModels.get(obj);

        ObjModel o = new ObjModel(obj);
        loadedObjModels.put(obj, o);
        return o;
    }

    ObjModel(Identifier identifier) {
        Identifier objId = Identifier.of(identifier.getNamespace(), identifier.getPath() + ".obj");
        Identifier jsonId = Identifier.of(identifier.getNamespace(), identifier.getPath() + ".json");

        try {
            this.obj = ObjRenderer.loadObj(objId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try loading JSON config if it exists
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        Optional<Resource> resource = resourceManager.getResource(jsonId);

        if (resource.isPresent()) {
            try (InputStream stream = resource.get().getInputStream()) {
                this.config = MAPPER.readValue(stream, Config.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void render(Map<String, Mtl> mtlMap, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            int overlay) {
        matrices.push();

        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));

        if (this.config != null) {
            matrices.translate(config.transform.x, config.transform.y, config.transform.z);

            matrices.scale(config.scale.x, config.scale.y, config.scale.z);

            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(config.rotation.x));
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(config.rotation.y));
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Z.rotationDegrees(config.rotation.z));
        }

        ObjRenderer.renderObj(this.obj, mtlMap, matrices, vertexConsumers, light, overlay);

        matrices.pop();
    }
}
