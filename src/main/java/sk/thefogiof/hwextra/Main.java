package sk.thefogiof.hwextra;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.thefogiof.hwextra.feature.FriendsWeb;
import sk.thefogiof.hwextra.register.*;
import sk.thefogiof.hwextra.screen.HudSettingsScreen;
import sk.thefogiof.hwextra.register.ApiKey;
import sk.thefogiof.hwextra.feature.AutoSaleParser;
import sk.thefogiof.hwextra.utils.FeatureControl;
import sk.thefogiof.hwextra.utils.TaskScheduler;

public class Main implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("HolyWorldExtra");

    public static KeyMapping key = new KeyMapping("Настройки HUD", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, KeyMapping.Category.CREATIVE);

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(TaskScheduler::tick);
        ClientLifecycleEvents.CLIENT_STARTED.register(Shaders::register);
        Configs.register();
        ApiKey.register();
        Huds.register();
        AutoSaleParser.registerEvent();
        Commands.register();
        FriendsWeb.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (key.consumeClick()) client.setScreen(new HudSettingsScreen());
        });

        FeatureControl.register();
    }
}