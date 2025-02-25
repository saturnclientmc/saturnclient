package org.auraclient.auraclient.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public abstract class AuraUi extends Screen {
    public final ArrayList<AuraWidget> widgets = new ArrayList<>();

    protected AuraUi(Text title) {
        super(title);
    }

    @Override
    protected void init() {}

    public void ui(DrawContext context) {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        widgets.clear();

        ui(context);

        for (AuraWidget widget : widgets) {
            widget.client = client;
            widget.render(context, isInside(mouseX, mouseY, widget.x, widget.y, widget.x + widget.width, widget.y + widget.height));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (AuraWidget widget : widgets) {
                if (isInside(mouseX, mouseY, widget.x, widget.y, widget.x + widget.width, widget.y + widget.height)) {
                    widget.onClick.run();
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isInside(double mouseX, double mouseY, int x1, int y1, int x2, int y2) {
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }
}
