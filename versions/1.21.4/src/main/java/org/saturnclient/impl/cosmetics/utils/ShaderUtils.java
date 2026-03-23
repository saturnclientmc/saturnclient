package org.saturnclient.impl.cosmetics.utils;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderPhase.Textures;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.util.Identifier;

public class ShaderUtils {
    public static final RenderLayer END_GATEWAY = RenderLayer.of(
            "end_gateway",
            VertexFormats.POSITION,
            DrawMode.QUADS,
            1536,
            false,
            false,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(RenderLayer.END_GATEWAY_PROGRAM)
                    .texture(Textures.create()
                            .add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false)
                            .add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false)
                            .build())
                    .cull(RenderPhase.DISABLE_CULLING)
                    .build(false));

    public static RenderLayer getRenderLayer(Identifier texture) {
        if (texture.getPath().endsWith("end.png")) {
            return END_GATEWAY;
        } else {
            return RenderLayer.getEntityAlpha(texture);
        }
    }
}
