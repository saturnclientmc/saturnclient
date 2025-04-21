package org.saturnclient.ui2.elements;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.util.Identifier;

public class Button extends Element {
    private String text;
    private Identifier font;

    public Button(String text, boolean bold) {
        this.text = text;
        this.font = Fonts.getFont(bold);
        this.dimensions(Fonts.getWidth(text, this.font)+ 25 , Fonts.getHeight() + 10);
        this.color = 0xFFFFFFFF;
    }

    @Override
    public void render(RenderScope renderScope) {
        renderScope.drawRoundedRectangle(this.x, this.y, 200, 100, 10, this.color);
        // renderScope.drawText(this.text, this.x, this.y, false, this.color);
    }
}
