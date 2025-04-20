package org.saturnclient.ui2.elements;

import org.joml.Quaternionf;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderScope;

public class Text extends Element {
    private String text;

    private boolean logged = false;

    public Text(String text) {
        this.text = text;
        this.color = 0xFFFFFFFF;
    }

    @Override
    public void render(RenderScope renderScope) {
        // renderScope.drawText(this.text, this.x, this.y, this.color);

        int rectWidth = 300;
        int rectHeight = 300;

        renderScope.matrices.push();

        renderScope.matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Z.rotationDegrees(90.0f));

        renderScope.matrices.translate(300, 300, 0);

        renderScope.matrices.scale(0.1f, 0.1f, 1.0f);


        int r = 90; // Radius of the corner
        int h = (rectHeight / 2) * 10; // Height of the box
        int w = (rectWidth / 2) * 10; // Width of the box

        for (int y = 0; y < h; y++) {
            int startX;

            if (y < r) {
                double dy = y - r;
                double dx = r - Math.sqrt(r * r - dy * dy);
                startX = (int) Math.ceil(dx);
            } else {
                startX = 0;
            }

            renderScope.drawRect(startX, y, w - startX, 1, this.color);
        }

        renderScope.matrices.pop();
    }
}
