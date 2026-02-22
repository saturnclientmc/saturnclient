package org.saturnclient.ui2.resources;

import java.awt.Graphics2D;
import java.awt.Rectangle;
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
        // Create SVGUniverse and load the SVG
        SVGUniverse universe = new SVGUniverse();
        java.net.URI uri = universe.loadSVG(svgStream, "tempSvg");
        SVGDiagram diagram = universe.getDiagram(uri);

        // Create a BufferedImage to render the SVG
        BufferedImage image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Set the diagram's viewport to match the desired size
        diagram.setIgnoringClipHeuristic(true);
        diagram.setDeviceViewport(new Rectangle(0, 0, width, height));

        // Render the SVG onto the BufferedImage
        diagram.render(g);

        g.dispose();
        return image;
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