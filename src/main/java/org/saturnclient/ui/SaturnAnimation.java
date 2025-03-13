package org.saturnclient.ui;

public enum SaturnAnimation {
    FADE,
    SLIDE,
    FADE_SLIDE;

    public int offset = 10;
    public float speed = 0.05f;

    public void apply(SaturnWidget widget) {
        switch (this) {
            case FADE:
                widget.alpha = Math.min(widget.alpha + speed, 1.0f);
                break;
            case SLIDE:
                if (offset > 0) {
                    widget.y--;
                    offset--;
                }
                break;
            case FADE_SLIDE:
                widget.alpha = Math.min(widget.alpha + speed, 1.0f);
                if (offset > 0) {
                    widget.y--;
                    offset--;
                }
                break;
        }
    }

    public SaturnAnimation offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SaturnAnimation speed(float speed) {
        this.speed = speed;
        return this;
    }
}
