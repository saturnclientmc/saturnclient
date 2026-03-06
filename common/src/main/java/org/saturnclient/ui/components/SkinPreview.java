package org.saturnclient.ui.components;

import org.saturnclient.ui.Element;
import org.saturnclient.ui.ElementContext;
import org.saturnclient.ui.RenderScope;

public class SkinPreview extends Element {
    public static EntityDrawer DRAWER;
    private boolean negativeAngle = false;
    private float angle = 0.0f;

    public SkinPreview(float angle, boolean negativeAngle) {
        this.angle = angle;
        this.negativeAngle = negativeAngle;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        DRAWER.render(renderScope, negativeAngle, angle, scale, ctx);
    }

    public static interface EntityDrawer {
        public void render(RenderScope renderScope, boolean negativeAngle, float angle, float scale,
                ElementContext ctx);
    }
}
