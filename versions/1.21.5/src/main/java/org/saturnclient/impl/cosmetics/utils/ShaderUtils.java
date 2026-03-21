package org.saturnclient.impl.cosmetics.utils;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ShaderUtils {
    public static RenderLayer getRenderLayer(Identifier texture) {
        if (texture.getPath().endsWith("end.png")) {
            return RenderLayer.getEndGateway();
        } else {
            return RenderLayer.getEntityAlpha(texture);
        }
    }
}