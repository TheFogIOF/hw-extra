package sk.thefogiof.hwextra.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.config.HudConfig;
import sk.thefogiof.hwextra.model.HudPosition;
import sk.thefogiof.hwextra.utils.FormatUtils;
import sk.thefogiof.hwextra.utils.RenderUtils;

import java.util.ArrayList;
import java.util.Collections;

public class PosHud {
    private static int x = (int) HudConfig.getInstance().posHudPosition.x;
    private static int y = (int) HudConfig.getInstance().posHudPosition.y;
    private static HudPosition.Align align = HudConfig.getInstance().posHudPosition.align;

    private static boolean disableRender = false;
    private static boolean isDragging, canDrag = false;
    private static Vector2f dragOffset = new Vector2f(0, 0);

    private static int hudWidth = 0;
    private static int hudHeight = 0;

    private static final int LINE_HEIGHT = 8;
    private static final int MARGIN = 2;
    private static final int ROW_HEIGHT = MARGIN * 2 + LINE_HEIGHT;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (disableRender) return;
        renderHud(guiGraphics);
    }

    public static void renderHud(GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();
        Font font = client.font;

        if (client.player != null) {
            Component hudText = Component.literal("x: " + Math.round(client.player.position().x) + " y: " + Math.round(client.player.position().y) + " z: " + Math.round(client.player.position().z));
            hudWidth = font.width(hudText);
            hudHeight = ROW_HEIGHT;

            int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
            int aX = abs[0], aY = abs[1];

            guiGraphics.fill(aX, aY, aX + hudWidth + MARGIN * 2, aY + ROW_HEIGHT, 0x960e0e0e);
            guiGraphics.drawCenteredString(font, hudText, aX + hudWidth / 2 + MARGIN, aY + MARGIN,0xffffffff);
        }
    }

    public static void setCanDrag(double mouseX, double mouseY) {
        int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
        int aX = abs[0], aY = abs[1];
        dragOffset.set((float) mouseX - aX, (float) mouseY - aY);
        canDrag = mouseX >= aX && mouseX <= aX + hudWidth + MARGIN * 2 && mouseY >= aY && mouseY <= aY + hudHeight;
    }

    public static void handleDragging(double mouseX, double mouseY, boolean isLeftButton) {
        if (!canDrag) return;
        if (isLeftButton && !isDragging) {
            int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
            dragOffset.set((float) mouseX - abs[0], (float) mouseY - abs[1]);
            isDragging = true;
        }

        if (isDragging) {
            if (isLeftButton) {
                int targetAbsX = (int) (mouseX - dragOffset.x);
                int targetAbsY = (int) (mouseY - dragOffset.y);

                HudPosition.Align newAlign = getAlign(mouseX, mouseY);
                if (newAlign != align) align = newAlign;

                int[] newRel = RenderUtils.toRelative(targetAbsX, targetAbsY, align, hudWidth, hudHeight);
                x = newRel[0]; y = newRel[1];
                if (x < 0) x = 0; if (y < 0) y = 0;
            } else {
                HudConfig.getInstance().posHudPosition.x = x;
                HudConfig.getInstance().posHudPosition.y = y;
                HudConfig.getInstance().posHudPosition.align = align;
                HudConfig.getInstance().save();
                isDragging = false;
            }
        }
    }

    private static HudPosition.@NonNull Align getAlign(double mouseX, double mouseY) {
        Minecraft client = Minecraft.getInstance();
        int screenW = client.getWindow().getGuiScaledWidth();
        int screenH = client.getWindow().getGuiScaledHeight();
        HudPosition.Align newAlign;
        if (mouseX < screenW / 2f) newAlign = (mouseY < screenH / 2f) ? HudPosition.Align.TOP_LEFT : HudPosition.Align.BOTTOM_LEFT;
        else newAlign = (mouseY < screenH / 2f) ? HudPosition.Align.TOP_RIGHT : HudPosition.Align.BOTTOM_RIGHT;
        return newAlign;
    }

    public static boolean isDisabledRender() {
        return disableRender;
    }

    public static void setDisabledRender(boolean value) {
        PosHud.disableRender = value;
    }
}