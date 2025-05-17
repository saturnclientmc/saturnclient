package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ClickableTexture extends Element {
    private static ThemeManager theme = new ThemeManager("ClickableTexture", "hovering");
    private static Property<Integer> fgColor = theme.property("fg-color", new Property<Integer>(0xFFFFFFFF));

    Identifier sprite;
    private Runnable onClick;

    public ClickableTexture(Identifier sprite, Runnable onClick) {
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
        
        // renderScope.setRenderLayer(RenderLayer::getGuiTextured);
        renderScope.drawTexture(sprite, 0, 0, 0, 0, width, height, fgColor.value);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
