package org.saturnclient.impl.cosmetics.utils;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.cosmetics.utils.GifUtils;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.util.Identifier;

public class ShaderUtils {
    // public static final RenderPipeline END_GATEWAY_PIPELINE =
    // RenderPipelines.register(
    // RenderPipeline.builder(new RenderPipeline.Snippet[] {
    // RenderPipelines.RENDERTYPE_END_PORTAL_SNIPPET })
    // .withLocation("pipeline/end_gateway")
    // .withShaderDefine("PORTAL_LAYERS", 16)
    // .withCull(false)
    // .build());

    // public static final RenderLayer END_GATEWAY = RenderLayer.of("end_gateway",
    // 1536, false, false,
    // END_GATEWAY_PIPELINE,
    // RenderLayer.MultiPhaseParameters.builder()
    // .texture(Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE,
    // false)
    // .add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false).build())
    // .build(false));

    public static RenderLayer getRenderLayer(IdentifierRef texture) {
        String path = texture.toString();
        if (path.endsWith(".gif")) {
            return RenderLayers.entityAlpha((Identifier) (Object) GifUtils.get(texture));
        } else if (path.endsWith("end.png")) {
            // return END_GATEWAY;
            return RenderLayers.endGateway();
        } else {
            return RenderLayers.entityAlpha((Identifier) (Object) texture);
        }
    }
}