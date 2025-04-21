package org.saturnclient.ui2.elements;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.util.Identifier;

public class Button extends Element {
    private String text;
    private boolean bold;
    private Identifier font;

    public Button(String text, boolean bold) {
        this.text = text;
        this.bold = bold;
        this.font = Fonts.getFont(bold);
        this.dimensions(Fonts.getWidth(text, font)+ 25 , Fonts.getHeight() + 10);
        this.color = 0xFFFFFFFF;
    }

    @Override
    public void render(RenderScope renderScope) {
        renderScope.drawRoundedRectangle(0, 0, width, height, 10, 0xFF000000);

        renderScope.drawText(text, width / 2 - Fonts.getWidth(text, font) / 2, height / 2 - Fonts.getHeight() / 2, bold, color);
    }
}
