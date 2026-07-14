package sk.thefogiof.hwextra.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.config.HudConfig;
import sk.thefogiof.hwextra.model.HudPosition;
import sk.thefogiof.hwextra.register.Configs;
import sk.thefogiof.hwextra.utils.FeatureControl;
import sk.thefogiof.hwextra.utils.FormatUtils;
import sk.thefogiof.hwextra.utils.RenderUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;

public class FarmerHud {
    private static int x = (int) HudConfig.getInstance().farmingHudPosition.x;
    private static int y = (int) HudConfig.getInstance().farmingHudPosition.y;
    private static HudPosition.Align align = HudConfig.getInstance().farmingHudPosition.align;

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

        long money = 0;
        try { money = Long.parseLong(Configs.getHudMoney()); } catch (Exception ignored) { }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#,###", symbols);

        String formatedMoney = (Configs.mainConfig.shortNumbers) ? FormatUtils.formatNumber(String.valueOf(money)) : df.format(money);
        String formatedBuyerExp = (Configs.mainConfig.shortNumbers) ? FormatUtils.formatNumber(Configs.getBuyerExp().toString()) : df.format(Configs.getBuyerExp());

        Component hudNameText = Component.literal("ꜰᴀʀᴍᴇʀ ʜᴜᴅ");

        Component moneyText = Component.literal(" " + formatedMoney).withColor(0xffff00);
        Component hudMoneyText = Component.literal("Монеток:").append(moneyText);

        Component buyerExpText = Component.literal(" " + formatedBuyerExp).withColor(0x00ff00);
        Component hudBuyerExpText = Component.literal("Опыт скупщика:").append(buyerExpText);

        ArrayList<Integer> fontWidths = new ArrayList<>();
        fontWidths.add(font.width(hudNameText));
        fontWidths.add(font.width(hudMoneyText));
        fontWidths.add(font.width(hudBuyerExpText));

        hudWidth = Collections.max(fontWidths);
        hudHeight = ROW_HEIGHT * 3;

        int[] abs = RenderUtils.toAbsolute(x, y, align, hudWidth, hudHeight);
        int aX = abs[0], aY = abs[1];

        // hud name
        guiGraphics.fill(aX, aY, aX + hudWidth + MARGIN * 2, aY + ROW_HEIGHT, 0x960e0e0e);
        guiGraphics.drawCenteredString(font, hudNameText, aX + hudWidth / 2 + MARGIN, aY + MARGIN,0xffffffff);
        // first line (money)
        guiGraphics.fill(aX, aY + ROW_HEIGHT, aX + hudWidth + MARGIN * 2, aY + ROW_HEIGHT * 2, 0x960e0e0e);
        guiGraphics.drawString(font, hudMoneyText, aX + MARGIN, aY + ROW_HEIGHT + MARGIN,0xffffffff, false);
        // second line (buyer exp)
        guiGraphics.fill(aX, aY + ROW_HEIGHT * 2, aX + hudWidth + MARGIN * 2, aY + ROW_HEIGHT * 3, 0x960e0e0e);
        guiGraphics.drawString(font, hudBuyerExpText, aX + MARGIN, aY + ROW_HEIGHT * 2 + MARGIN,0xffffffff, false);
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
                HudConfig.getInstance().farmingHudPosition.x = x;
                HudConfig.getInstance().farmingHudPosition.y = y;
                HudConfig.getInstance().farmingHudPosition.align = align;
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
        FarmerHud.disableRender = value;
    }
}