package org.saturnclient.impl.cosmetics.utils;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.saturnclient.SaturnClient;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Map;

public class IdentifierUtils {

    public static void registerBufferedImageTextureFast(IdentifierRef identifierRef, BufferedImage bufferedImage) {
        try {
            Identifier identifier = (Identifier) (Object) identifierRef;

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);

            if (bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB) {
                int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

                int index = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        nativeImage.setColorArgb(x, y, pixels[index++]);
                    }
                }

            } else {

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        nativeImage.setColorArgb(x, y, bufferedImage.getRGB(x, y));
                    }
                }

            }

            NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);

            SaturnClient.client.execute(() ->
                SaturnClient.client.getTextureManager().registerTexture(identifier, texture)
            );

        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to register texture: {}", identifierRef, e);
        }
    }

    public static void registerBufferedImageTexturesBulk(Map<IdentifierRef, BufferedImage> textures) {

        long start = System.currentTimeMillis();

        textures.forEach(IdentifierUtils::registerBufferedImageTextureFast);

        long end = System.currentTimeMillis();

        SaturnClient.LOGGER.info(
            "Registered {} textures in {}ms",
            textures.size(),
            (end - start)
        );
    }
}