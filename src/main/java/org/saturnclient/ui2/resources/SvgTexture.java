package org.saturnclient.ui2.resources;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.cosmetics.cloaks.utils.IdentifierUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class SvgTexture {

    private static BufferedImage renderSvg(InputStream svgStream, int width, int height) throws Exception {
        // Load SVG
        SVGUniverse universe = new SVGUniverse();
        java.net.URI uri = universe.loadSVG(svgStream, "tempSvg");
        SVGDiagram diagram = universe.getDiagram(uri);

        // Get intrinsic size of SVG
        float svgWidth = (float) diagram.getWidth();
        float svgHeight = (float) diagram.getHeight();

        // Compute scale factors
        float scaleX = width / svgWidth;
        float scaleY = height / svgHeight;

        // Create output image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Enable anti-aliasing for smooth scaling
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Apply scaling transform
        g.scale(scaleX, scaleY);

        // Render SVG
        diagram.render(g);

        g.dispose();
        return image;
    }

    @Nullable
    @SuppressWarnings("resource")
    public static Identifier getSvg(MinecraftClient client, Identifier svgImage, int width, int height) {
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