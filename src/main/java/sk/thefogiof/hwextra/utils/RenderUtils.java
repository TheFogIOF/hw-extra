package sk.thefogiof.hwextra.utils;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import sk.thefogiof.hwextra.model.HudPosition;

public class RenderUtils {
    private static final int MARGIN = 2;

    public enum ScaleType {
        WIDTH, HEIGHT
    }

    public static float toScale(Window window, float size, ScaleType scaleType) {
        float scaleW = window.getWidth() / (float) window.getGuiScale() / 1920f;
        float scaleH = window.getHeight() / (float) window.getGuiScale() / 1080f;

        if (scaleType == ScaleType.WIDTH) return size * scaleW;
        if (scaleType == ScaleType.HEIGHT) return size * scaleH;

        return size;
    }

    public static void drawScaleString(GuiGraphics graphics, Font font, Component component, int x, int y, int color, boolean bl, float scale) {
        graphics.pose().pushMatrix();
        graphics.pose().scale(scale, scale);
        graphics.drawString(font, component, (int) (x / scale), (int) (y / scale), color, bl);
        graphics.pose().popMatrix();
    }

    public static void drawScaleCenteredString(GuiGraphics graphics, Font font, Component component, int x, int y, int color, float scale) {
        graphics.pose().pushMatrix();
        graphics.pose().scale(scale, scale);
        graphics.drawCenteredString(font, component, (int) (x / scale), (int) (y / scale), color);
        graphics.pose().popMatrix();
    }

    /** Абсолютная позиция левого-верхнего угла HUD по относительным координатам и заданным размерам. */
    public static int[] toAbsolute(int relX, int relY, HudPosition.Align align, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();
        int absX, absY;
        switch (align) {
            case TOP_LEFT -> { absX = relX; absY = relY; }
            case TOP_RIGHT -> { absX = screenW - relX - width - MARGIN * 2; absY = relY; }
            case BOTTOM_LEFT -> { absX = relX; absY = screenH - relY - height; }
            case BOTTOM_RIGHT -> { absX = screenW - relX - width - MARGIN * 2; absY = screenH - relY - height; }
            default -> throw new IllegalArgumentException("Unknown alignment: " + align);
        }
        return new int[]{absX, absY};
    }

    /** Относительные координаты из абсолютных, с учётом указанных размеров HUD. */
    public static int[] toRelative(int absX, int absY, HudPosition.Align align, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();
        int relX, relY;
        switch (align) {
            case TOP_LEFT -> { relX = absX; relY = absY; }
            case TOP_RIGHT -> { relX = screenW - absX - width - MARGIN * 2; relY = absY; }
            case BOTTOM_LEFT -> { relX = absX; relY = screenH - absY - height; }
            case BOTTOM_RIGHT -> { relX = screenW - absX - width - MARGIN * 2; relY = screenH - absY - height; }
            default -> throw new IllegalArgumentException("Unknown alignment: " + align);
        }
        return new int[]{relX, relY};
    }
}
