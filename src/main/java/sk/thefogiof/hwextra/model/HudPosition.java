package sk.thefogiof.hwextra.model;

public class HudPosition {
    public enum Align {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    public float x;
    public float y;
    public Align align;

    public HudPosition(float x, float y, Align align) {
        this.x = x;
        this.y = y;
        this.align = align;
    }

    public static HudPosition Empty() {
        return new HudPosition(0, 0, Align.TOP_LEFT);
    }
}
