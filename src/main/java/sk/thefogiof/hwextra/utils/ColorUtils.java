package sk.thefogiof.hwextra;

public class ColorUtils {
    public static int lerpColor(int current, int target, float step) {
        if (current == target) return current;

        int a1 = (current >> 24) & 0xFF;
        int r1 = (current >> 16) & 0xFF;
        int g1 = (current >> 8) & 0xFF;
        int b1 = current & 0xFF;

        int a2 = (target >> 24) & 0xFF;
        int r2 = (target >> 16) & 0xFF;
        int g2 = (target >> 8) & 0xFF;
        int b2 = target & 0xFF;

        float a = approach(a1, a2, step);
        float r = approach(r1, r2, step);
        float g = approach(g1, g2, step);
        float b = approach(b1, b2, step);

        // Собираем обратно в int (ARGB)
        return ((int) a << 24) | ((int) r << 16) | ((int) g << 8) | (int) b;
    }

    private static float approach(float current, float target, float step) {
        if (current < target) {
            return Math.min(current + step, target);
        } else {
            return Math.max(current - step, target);
        }
    }
}
