package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class Button extends Element {
    private static ThemeManager theme = new ThemeManager("Button", "hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(0xFF000000));
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(0xFFFFFFFF));
    private static Property<Boolean> bold = theme.property("fg-bold", new Property<Boolean>(false));

    static {
        theme.propertyStateDefault("hovering", "fg-color", 0xFFFF0000);
    }

    private String text;
    private Identifier font;
    private Runnable onClick;

    public Button(String text, Runnable onClick) {
        this.text = text;
        this.font = Fonts.getFont(bold.value);
        this.onClick = onClick;
        this.dimensions(Fonts.getWidth(text, font)+ 25 , Fonts.getHeight() + 10);
    }

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
        int w = width;
        int h = width;
        int r = 30;
        int s = 1;

        renderScope.drawRect(0, 0, w, h, bgColor.value);

        renderScope.drawRoundedBorder(w, h, s, r, fgColor.value);

        // renderScope.drawRoundedSide(w - (r / 2 + s - 1), 0, w, h, s, r, fgColor.value); // right

        // renderScope.matrices.push();
        // renderScope.matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));

        // renderScope.matrices.pop();

        // if (ctx.isHovering()) {
        //     theme.setState("hovering");
        // } else {
        //     theme.setState(null);
        // }
        // renderScope.drawRoundedRectangle(0, 0, width, height, 10, bgColor.value);

        // renderScope.drawText(text, width / 2 - Fonts.getWidth(text, font) / 2, height / 2 - Fonts.getHeight() / 2, bold.value, fgColor.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
