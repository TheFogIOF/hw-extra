package sk.thefogiof.hwextra.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.*;
import org.jspecify.annotations.NonNull;

import java.util.logging.Logger;

public class ButtonWidget extends AbstractWidget {
    public ButtonWidget(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        //guiGraphics.pose().pushMatrix();
        //guiGraphics.pose().scale(scale, scale);
        guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF00FF00);
        //guiGraphics.pose().popMatrix();
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (this.isActive()) {
            if (mouseButtonEvent.x() >= getX() && mouseButtonEvent.x() <= (getX() + getWidth())) {
                if (mouseButtonEvent.y() >= getY() && mouseButtonEvent.y() <= (getY() + getHeight())) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    this.onClick(mouseButtonEvent, bl);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {

    }
}