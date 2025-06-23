package org.saturnclient.ui2.elements;

import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.ElementContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.anim.SlideX;
import org.saturnclient.ui2.resources.Fonts;
import org.saturnclient.ui2.resources.Textures;

public class Notification extends Element {
    private static ThemeManager theme = new ThemeManager("Notification");
    private static Property<Integer> bgColor = theme.property("bg-color", Property.color(0xFF000000));
    private static Property<Integer> error = theme.property("error", Property.color(0xFFbf212f));
    private static Property<Integer> info = theme.property("info", Property.color(0xFF264b96));
    private static Property<Integer> success = theme.property("success", Property.color(0xFF27b376));

    public static enum NotificationKind {
        Error,
        Info,
        Success,
    }

    NotificationKind kind;
    String text;
    String title;

    public Notification(int width, int height, NotificationKind kind, String title, String text) {
        this.kind = kind;
        this.text = text;
        this.title = title;
        this.width = (int) Math.max(Fonts.getWidth(text, 1) * 0.6f, Fonts.getWidth(title, 1)) + 20;
        this.height = 50;
        this.x = width - this.width - 15;
        this.y = height - this.height - 15;
        this.animation = new SlideX(700, -width);
        this.duration = 3000;
    }

    @Override
    public void render(RenderScope renderScope, ElementContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, 10, bgColor.value);
        switch (kind) {
            case Error:
                renderScope.drawTexture(Textures.CIRCLE_X, 9, 9, 0, 0, 15, 15, error.value);
                break;

            case Info:
                renderScope.drawTexture(Textures.INFO, 9, 9, 0, 0, 15, 15, info.value);
                break;

            case Success:
                renderScope.drawTexture(Textures.CHECK, 9, 9, 0, 0, 15, 15, success.value);
                break;
        }
        renderScope.drawText(0.7f, title, 32, 10, 1, -1);
        renderScope.drawText(0.5f, text, 32, 28, 1, -1);
    }
}
