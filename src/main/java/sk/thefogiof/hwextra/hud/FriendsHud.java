package sk.thefogiof.hwextra.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;
import sk.thefogiof.hwextra.config.HudConfig;
import sk.thefogiof.hwextra.model.Friend;
import sk.thefogiof.hwextra.model.HudPosition;
import sk.thefogiof.hwextra.feature.Friends;
import sk.thefogiof.hwextra.utils.RenderUtils;

import java.util.ArrayList;
import java.util.Collections;

public class FriendsHud {
    private static int x = (int) HudConfig.getInstance().friendsHudPosition.x;
    private static int y = (int) HudConfig.getInstance().friendsHudPosition.y;
    private static HudPosition.Align align = HudConfig.getInstance().friendsHudPosition.align;

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

        ArrayList<Component> lines = new ArrayList<>();
        lines.add(Component.literal("ꜰʀɪᴇɴᴅꜱ"));
        for (Friend friend : Friends.friends) {
            int color = (friend.online) ? 0x00ff00 : 0x4d4d4d;
            lines.add(Component.literal("● ").withColor(color).append(Component.literal(friend.name).withColor(0xffffff)).append(Component.literal(" (" + friend.server + ")").withColor(0x888888)));
        }

        ArrayList<Integer> widths = new ArrayList<>();
        for (Component line : lines) widths.add(font.width(line));
        hudWidth = Collections.max(widths);
        hudHeight = ROW_HEIGHT * lines.size();

        int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
        int aX = abs[0], aY = abs[1];

        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                guiGraphics.fill(aX, aY, aX + hudWidth + (MARGIN * 2), aY + ROW_HEIGHT, 0x960e0e0e);
                guiGraphics.drawCenteredString(font, lines.get(i), aX + (hudWidth / 2) + MARGIN, aY + MARGIN,0xffffffff);
            } else {
                guiGraphics.fill(aX, aY + ROW_HEIGHT * i, aX + hudWidth + (MARGIN * 2), (aY + ROW_HEIGHT * (i + 1)), 0x960e0e0e);
                guiGraphics.drawString(font, lines.get(i), aX + MARGIN, (aY + ROW_HEIGHT * i) + MARGIN,0xffffffff, false);
            }
        }
    }

    public static void setCanDrag(double mouseX, double mouseY) {
        int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
        int aX = abs[0], aY = abs[1];
        dragOffset.set((float) mouseX - aX, (float) mouseY - aY);
        canDrag = mouseX >= aX && mouseX <= aX + hudWidth + MARGIN * 2 && mouseY >= aY && mouseY <= aY + hudHeight;
    }

    public static void handleDragging(double mouseX, double mouseY, boolean leftButton) {
        if (!canDrag) return;

        if (leftButton && !isDragging) {
            int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
            dragOffset.set((float) mouseX - abs[0], (float) mouseY - abs[1]);
            isDragging = true;
        }

        if (isDragging) {
            if (leftButton) {
                int targetAbsX = (int) (mouseX - dragOffset.x);
                int targetAbsY = (int) (mouseY - dragOffset.y);

                HudPosition.Align newAlign = getAlign(mouseX, mouseY);
                if (newAlign != align) align = newAlign;

                int[] newRel = RenderUtils.toRelative(targetAbsX, targetAbsY, align, hudWidth, hudHeight);
                x = newRel[0]; y = newRel[1];
                if (x < 0) x = 0; if (y < 0) y = 0;
            } else {
                HudConfig.getInstance().friendsHudPosition.x = x;
                HudConfig.getInstance().friendsHudPosition.y = y;
                HudConfig.getInstance().friendsHudPosition.align = align;
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
        FriendsHud.disableRender = value;
    }
}