package sk.thefogiof.hwextra.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import sk.thefogiof.hwextra.hud.EffectHud;
import sk.thefogiof.hwextra.hud.FarmerHud;
import sk.thefogiof.hwextra.hud.FriendsHud;
import sk.thefogiof.hwextra.hud.PosHud;

public class HudSettingsScreen extends Screen {
    private boolean dragging;

    public HudSettingsScreen() {
        super(Component.literal("Настройки HUD"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fill(0, 0, width, height, 0x88000000);

        guiGraphics.drawString(font, Component.literal("Перетащите HUD в нужное место"), 10, 10, 0xFFFFFF, false);
        guiGraphics.drawString(font, Component.literal("Нажмите ESC для выхода"), 10, 25, 0xAAAAAA, false);

        FarmerHud.renderHud(guiGraphics);
        FriendsHud.renderHud(guiGraphics);
        EffectHud.renderHud(guiGraphics);
        PosHud.renderHud(guiGraphics);

        FarmerHud.setDisabledRender(true);
        FriendsHud.setDisabledRender(true);
        EffectHud.setDisabledRender(true);
        PosHud.setDisabledRender(true);

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        FarmerHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        FarmerHud.setCanDrag(mouseButtonEvent.x(), mouseButtonEvent.y());

        FriendsHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        FriendsHud.setCanDrag(mouseButtonEvent.x(), mouseButtonEvent.y());

        EffectHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        EffectHud.setCanDrag(mouseButtonEvent.x(), mouseButtonEvent.y());

        PosHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        PosHud.setCanDrag(mouseButtonEvent.x(), mouseButtonEvent.y());

        return super.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double x, double y) {
        FarmerHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        FriendsHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        EffectHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        PosHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), true);
        return super.mouseDragged(mouseButtonEvent, x, y);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        FarmerHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), false);
        FriendsHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), false);
        EffectHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), false);
        PosHud.handleDragging(mouseButtonEvent.x(), mouseButtonEvent.y(), false);
        return super.mouseReleased(mouseButtonEvent);
    }

    @Override
    public void onClose() {
        FarmerHud.setDisabledRender(false);
        FriendsHud.setDisabledRender(false);
        EffectHud.setDisabledRender(false);
        PosHud.setDisabledRender(false);
        this.minecraft.setScreen((Screen)null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}