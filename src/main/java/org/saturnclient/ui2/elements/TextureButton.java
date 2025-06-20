package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class TextureButton extends Element {
    private static ThemeManager theme = new ThemeManager("TextureButton", "hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", Property.color(0xFF000000));
    private static Property<Integer> fgColor = theme.property("fg-color", Property.color(0xFFFFFFFF));
    private static Property<Integer> padding = theme.property("padding", Property.integer(14));
    private static Property<Integer> cornerRadius = theme.property("corner-radius", Property.integer(10));


    static {
        theme.propertyStateDefault("hovering", "fg-color", 0xFF845eee);
    }

    Identifier sprite;
    private Runnable onClick;

    public TextureButton(Identifier sprite, Runnable onClick) {
        this.sprite = sprite;
        this.onClick = onClick;
    }
    
    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        if (ctx.isHovering()) {
            theme.setState("hovering");
        } else {
            theme.setState(null);
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);
        
        renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        int texWidth = width - padding.value;
        int texHeight = height - padding.value;
        
        renderScope.drawTexture(sprite, padding.value / 2, padding.value / 2, 0, 0, texWidth, texHeight, fgColor.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
