package sk.thefogiof.hwextra.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoundedHud {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoundedHud.class);

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        int x = 0, y = 0, w = 100, h = 100;
        int radius = 4;
        int color = 0xffffffff;
    }
}