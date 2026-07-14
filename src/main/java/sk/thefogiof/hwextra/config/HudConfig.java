package sk.thefogiof.hwextra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.model.HudPosition;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class HudConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hwextra/hud.json");

    public HudPosition friendsHudPosition = HudPosition.Empty();
    public HudPosition farmingHudPosition = HudPosition.Empty();
    public HudPosition effectHudPosition = HudPosition.Empty();
    public HudPosition posHudPosition = HudPosition.Empty();

    private static HudConfig instance;

    public static HudConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static HudConfig load() {
        if (CONFIG_PATH.toFile().exists()) {
            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                try {
                    return GSON.fromJson(reader, HudConfig.class);
                } catch (Exception e) {
                    HudConfig config = new HudConfig();
                    config.save();
                    return config;
                }
            } catch (IOException e) {
                Main.LOGGER.info(e.getMessage());
            }
        }
        HudConfig config = new HudConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            Path parent = CONFIG_PATH.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            Main.LOGGER.info(e.getMessage());
        }
    }
}
