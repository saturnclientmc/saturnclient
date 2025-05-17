package org.saturnclient.ui2.components;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class CosmeticPreview extends Element {
    private static ThemeManager theme = new ThemeManager("CosmeticPreview", "selected", "hovering");
    private static Property<Integer> bgColor = theme.property("bg-color", new Property<Integer>(0xAA000000));
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(0xFFFFFFFF));
    private static Property<Integer> padding = theme.property("padding", new Property<Integer>(14));
    private static Property<Integer> cornerRadius = theme.property("corner-radius", new Property<Integer>(10));

    static {}

    Identifier sprite;
    private Runnable onClick;
    private boolean isSelected;

    public CosmeticPreview(boolean isSelected, Identifier sprite, Runnable onClick) {
        this.sprite = sprite;
        this.onClick = onClick;
        this.isSelected = isSelected;

        this.width = 50;
        this.height = 111;
    }
    
    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        if (ctx.isHovering()) {
            theme.setState("hovering");
        } else if (isSelected) {
            theme.setState("selected");
        }

        renderScope.drawRoundedRectangle(0, 0, width, height, cornerRadius.value, bgColor.value);
        
        // renderScope.setRenderLayer(RenderLayer::getGuiTextured);

        int texWidth = width - padding.value;
        int texHeight = height - padding.value;
        
        renderScope.drawTexture(sprite, padding.value / 2, padding.value / 2, 0, 0, texWidth, texHeight, fgColor.value);

        theme.setState(null);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
