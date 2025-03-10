package org.saturnclient.ui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SaturnUi extends Screen {
    public List<SaturnWidget> widgets = new ArrayList<>();

    public SaturnUi(Text title) {
        super(title);
    }

    public void draw(SaturnWidget widget) {
        widgets.add(widget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (SaturnWidget widget : widgets) {
            if (widget.animation != null) {
                widget.animation.apply(widget);
            }

            boolean isMouseInside = widget.x < mouseX && widget.x + widget.width > mouseX
                    && widget.y < mouseY && widget.y + widget.height > mouseY;
            widget.render(context, isMouseInside);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (SaturnWidget widget : widgets) {
                boolean isMouseInside = widget.x < mouseX && widget.x + widget.width > mouseX
                        && widget.y < mouseY && widget.y + widget.height > mouseY;
                if (isMouseInside) {
                    widget.click();
                }
            }
        }

        return false;
    }
}
