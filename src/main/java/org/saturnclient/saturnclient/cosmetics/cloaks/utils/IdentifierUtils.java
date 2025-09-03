package org.saturnclient.saturnclient.cosmetics.cloaks.utils;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class IdentifierUtils {

    /**
     * ORIGINAL METHOD - EXTREMELY SLOW - causes 10-20 second freezes
     * This converts BufferedImage -> PNG bytes -> ByteBuffer -> NativeImage ->
     * Texture
     * This is completely unnecessary and wastes massive amounts of CPU time!
     */
    public static void registerBufferedImageTexture(Identifier i, BufferedImage bi) {
        try {
            // This is the performance killer:
            // 1. Converts BufferedImage to PNG (expensive compression)
            // 2. Creates ByteBuffer copy (memory allocation)
            // 3. NativeImage.read() parses PNG back to raw pixels (expensive decompression)
            // This is doing compression -> decompression for no reason!

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bi, "png", baos);
            byte[] bytes = baos.toByteArray();
            java.nio.ByteBuffer bb = org.lwjgl.BufferUtils.createByteBuffer(bytes.length).put(bytes);
            bb.flip();
            NativeImageBackedTexture nibt = new NativeImageBackedTexture(NativeImage.read(bb));
            SaturnClient.client.getTextureManager().registerTexture(i, nibt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * OPTIMIZED METHOD - Direct pixel conversion without unnecessary PNG
     * encoding/decoding
     * This should reduce loading time from 10-20 seconds to under 1 second
     */
    public static void registerBufferedImageTextureFast(Identifier identifier, BufferedImage bufferedImage) {
        try {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            // Create NativeImage directly with the correct format
            NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);

            // Direct pixel copy - much faster than PNG conversion
            if (bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB) {
                // Fast path for ARGB images - direct memory access
                int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = pixels[y * width + x];
                        nativeImage.setColorArgb(x, y, pixel);
                    }
                }
            } else {
                // Fallback for other image types - still faster than PNG conversion
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgb = bufferedImage.getRGB(x, y);
                        nativeImage.setColorArgb(x, y, rgb);
                    }
                }
            }

            // Register the texture
            NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
            SaturnClient.client.execute(
                    () -> SaturnClient.client.getTextureManager().registerTexture(identifier, texture));

        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to register texture: " + identifier, e);
            // Fallback to slow method if fast method fails
            registerBufferedImageTexture(identifier, bufferedImage);
        }
    }

    /**
     * EVEN FASTER VERSION - Bulk texture registration
     * Use this when registering many textures at once (like animated cloak frames)
     */
    public static void registerBufferedImageTexturesBulk(java.util.Map<Identifier, BufferedImage> textures) {
        long startTime = System.currentTimeMillis();

        for (java.util.Map.Entry<Identifier, BufferedImage> entry : textures.entrySet()) {
            registerBufferedImageTextureFast(entry.getKey(), entry.getValue());
        }

        long endTime = System.currentTimeMillis();
        SaturnClient.LOGGER.info("Registered {} textures in {}ms", textures.size(), (endTime - startTime));
    }
}