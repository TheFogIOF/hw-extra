package sk.thefogiof.hwextra.screen;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import sk.thefogiof.hwextra.utils.RenderUtils;
import sk.thefogiof.hwextra.widgets.ButtonWidget;

public class MainScreen extends Screen {
    private static final int BASE_WIDTH = 1920;
    private static final int BASE_HEIGHT = 1080;
    private static final int PANEL_MARGIN = 20;

    private float scale;
    private int offsetX, offsetY;
    private int rightPanelX, rightPanelY, rightPanelW, rightPanelH;
    private int leftPanelW;

    private float scrollOffset, maxScroll = 0;

    protected MainScreen() {
        super(Component.literal("HolyWorld Extra"));
    }

    ButtonWidget buttonWidget;

    @Override
    protected void init() {
        buildGui();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        buildGui();
    }

    private void buildGui() {
        Window window = Minecraft.getInstance().getWindow();
        scale = Math.min((float) window.getGuiScaledWidth() / BASE_WIDTH, (float) window.getGuiScaledHeight() / BASE_HEIGHT);
        scale = Math.round(scale * 1000.0f) / 1000.0f;

        offsetX = (int) ((width - 1720 * scale) / 2f);
        offsetY = (int) ((height - 880 * scale) / 2f);
        rightPanelW = offsetX + (int) (1720 * scale);
        rightPanelH = offsetY + (int) (880 * scale);

        buttonWidget = new ButtonWidget((int) (680 * scale), (int) (20 * scale), (int) (100 * scale), (int) (60 * scale), Component.empty());
        this.addWidget(buttonWidget);
    }

    private float processScale(float value) {
        String stringValue = String.valueOf(value);
        String stringLastDigit = stringValue.substring(stringValue.length() - 1);
        int lastDigit = Integer.parseInt(stringLastDigit);
        if (lastDigit % 2 != 0) {
            StringBuilder sB = new StringBuilder("0.");
            sB.repeat("0", Math.max(0, stringValue.length() - 3));
            sB.append("1");
            return value + Float.parseFloat(sB.toString());
        }
        return value;
    }

    @Override
    public void render(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        float textScale = scale * 2f;
        // Основные панели
        graphics.fill(offsetX, offsetY, rightPanelW, rightPanelH,0xffff0f0f);

        graphics.drawString(font, Component.literal(scale + " " + textScale + " " + width + " " + height), 0, 0, 0xffffffff);
        graphics.pose().pushMatrix();
        graphics.pose().setTranslation((width - 1720 * scale) / 2f, (height - 880 * scale) / 2f);
        graphics.pose().scale(scale, scale);
        graphics.fill(0,0,1720,880,0xff0f0f0f);
        graphics.pose().popMatrix();


        graphics.pose().pushMatrix();
        graphics.pose().setTranslation((width - 1720 * scale) / 2f, (height - 880 * scale) / 2f);
        graphics.pose().scale(textScale, textScale);
        graphics.drawCenteredString(font, Component.literal("test"), 860 / 2, 0, 0xffffffff);
        graphics.pose().popMatrix();

        RenderUtils.drawScaleCenteredString(graphics, font, Component.literal("test2"), offsetX + (int) (860 * scale), (int) (40 * scale), 0xffffffff, textScale);

        buttonWidget.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}