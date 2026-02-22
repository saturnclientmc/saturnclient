package org.saturnclient.ui2.resources;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cosmetics.cloaks.utils.IdentifierUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class SvgTexture {
    private static BufferedImage renderSvg(InputStream svgStream, float width, float height) throws Exception {
        TranscoderInput input = new TranscoderInput(svgStream);

        final BufferedImage[] imagePointer = new BufferedImage[1];

        ImageTranscoder transcoder = new ImageTranscoder() {

            @Override
            public BufferedImage createImage(int w, int h) {
                return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            }

            @Override
            public void writeImage(BufferedImage img, org.apache.batik.transcoder.TranscoderOutput out) {
                imagePointer[0] = img;
            }
        };

        transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, width);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, height);

        transcoder.transcode(input, null);

        return imagePointer[0];
    }

    @Nullable
    @SuppressWarnings("resource")
    public static Identifier getSvg(MinecraftClient client, Identifier svgImage, int width, int height, float scale) {
        try (InputStream svgStream = client
                .getResourceManager()
                .getResource(svgImage).get()
                .getInputStream()) {

            BufferedImage image = renderSvg(svgStream, width, height);

            System.out.println(svgImage.toString());

            Identifier id = Identifier.of(SaturnClient.MOD_ID, "img_svg_render_" + (int) (Math.random() * 30));
            IdentifierUtils.registerBufferedImageTexture(id, image);

            return id;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
