package sk.thefogiof.hwextra.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.config.HudConfig;
import sk.thefogiof.hwextra.model.Friend;
import sk.thefogiof.hwextra.model.HudPosition;
import sk.thefogiof.hwextra.utils.FeatureControl;
import sk.thefogiof.hwextra.utils.RenderUtils;

import java.util.ArrayList;
import java.util.Collections;

public class EffectHud {
    private static int x = (int) HudConfig.getInstance().effectHudPosition.x;
    private static int y = (int) HudConfig.getInstance().effectHudPosition.y;
    private static HudPosition.Align align = HudConfig.getInstance().effectHudPosition.align;

    private static boolean disableRender, disabled = false;
    private static boolean isDragging, canDrag = false;
    private static Vector2f dragOffset = new Vector2f(0, 0);

    private static int hudWidth = 0;
    private static int hudHeight = 0;

    private static final int LINE_HEIGHT = 8;
    private static final int MARGIN = 2;
    private static final int ROW_HEIGHT = MARGIN * 2 + LINE_HEIGHT;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (disableRender || disabled) return;
        renderHud(guiGraphics);
    }

    private static String formatTime(int ticks) {
        if (ticks == -1) return "∞";
        int effectDuration = ticks / 20;
        int hours = effectDuration / 3600;
        int minutes = (effectDuration % 3600) / 60;
        int seconds = effectDuration % 60;
        if (hours == 0) return String.format("%02d:%02d", minutes, seconds);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void renderHud(GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();
        Font font = client.font;

        if (client.player == null) return;

        ArrayList<Component> lines = new ArrayList<>();
        lines.add(Component.literal("ᴇꜰꜰᴇᴄᴛꜱ"));
        for (MobEffectInstance effect : client.player.getActiveEffects()) {
            MutableComponent effectName = (MutableComponent) effect.getEffect().value().getDisplayName();
            int amplifier = effect.getAmplifier() + 1;
            int effectDuration = effect.getDuration();
            if (amplifier > 1) effectName.append(" " + amplifier);
            lines.add(effectName.withColor(0xffffff).append(Component.literal(" " + formatTime(effectDuration)).withColor(0x888888)));
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
        if (disabled) return;
        int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
        int aX = abs[0], aY = abs[1];
        dragOffset.set((float) mouseX - aX, (float) mouseY - aY);
        canDrag = mouseX >= aX && mouseX <= aX + hudWidth + MARGIN * 2 && mouseY >= aY && mouseY <= aY + hudHeight;
    }

    public static void handleDragging(double mouseX, double mouseY, boolean leftButton) {
        if (!canDrag || disabled) return;

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
                HudConfig.getInstance().effectHudPosition.x = x;
                HudConfig.getInstance().effectHudPosition.y = y;
                HudConfig.getInstance().effectHudPosition.align = align;
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

    public static void checkBlockList() {
        disabled = FeatureControl.BLOCKLIST.contains("effect_hud");
    }

    public static boolean isDisabledRender() {
        return disableRender;
    }

    public static void setDisabledRender(boolean value) {
        EffectHud.disableRender = value;
    }
}