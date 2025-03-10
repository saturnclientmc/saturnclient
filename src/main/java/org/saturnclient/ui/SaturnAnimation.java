package org.saturnclient.ui;

public enum SaturnAnimation {
    FADE,
    SLIDE,
    FADE_SLIDE;

    public int distance = 0;
    public int widgetY = 0;

    public void apply(SaturnWidget widget) {
        switch (this) {
            case FADE:
                widget.alpha = Math.min(widget.alpha + 0.05f, 1.0f);
                break;
            case SLIDE:
                if (widgetY < widget.y) {
                    widget.y -= 1;
                }
                break;
            case FADE_SLIDE:
                widget.alpha = Math.min(widget.alpha + 0.05f, 1.0f);
                if (widgetY < widget.y) {
                    widget.y -= 1;
                }
                break;
        }
    }

    public SaturnAnimation distance(int distance) {
        this.distance = distance;
        return this;
    }
}
