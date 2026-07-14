package sk.thefogiof.hwextra.register;

import net.minecraft.client.Minecraft;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.render.shader.Shader;

public class Shaders {
    private static Shader roundShader;
    private static boolean initialized = false;

    public static void register(Minecraft client) {
        if (initialized) return;
        try {
            roundShader = new Shader("core", "rounded_rect");
            initialized = true;
        } catch (Exception e) {
            Main.LOGGER.error(e.getMessage());
        }
    }

    public static Shader getRoundShader() {
        return roundShader;
    }
}
