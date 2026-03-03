package org.saturnclient.cosmetics.obj;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.javagl.obj.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class MtlLoader {
    static Map<Identifier, Map<String, Mtl>> loadedMtls = new HashMap<>();

    static Map<String, Mtl> loadMtl(Identifier mtlId) throws IOException {
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

    public static Map<String, Mtl> of(Identifier mtlId) {
        System.out.println(mtlId);
        if (loadedMtls.containsKey(mtlId))
            return loadedMtls.get(mtlId);

        try {
            Map<String, Mtl> mtl = loadMtl(mtlId);
            loadedMtls.put(mtlId, mtl);

            return mtl;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

    }

    public static Map<String, Mtl> cosmetic(String cosmeticId) {
        int index = cosmeticId.indexOf('_');

        String mtlId = (index == -1) ? "" : cosmeticId.substring(index + 1);

        if (mtlId.isEmpty())
            mtlId = "white";

        return of(Identifier.of("saturnclient", "models/cosmetic/materials/" + mtlId + ".mtl"));
    }
}
