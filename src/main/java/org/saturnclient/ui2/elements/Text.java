package org.saturnclient.ui2.elements;

import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderScope;

public class Text extends Element {
    private String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void render(RenderScope renderScope, boolean hovering) {
        renderScope.drawText(this.text, this.x, this.y, false, 0xFFFFFFFF);
    }
}
