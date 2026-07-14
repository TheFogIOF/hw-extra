package sk.thefogiof.hwextra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import sk.thefogiof.hwextra.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hwextra/main.json");

    public boolean shortNumbers = true;
    public String apiEndPoint = "http://localhost:5000";

    private static MainConfig instance;

    public static MainConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static MainConfig reload() {
        instance = load();
        return instance;
    }

    private static MainConfig load() {
        if (CONFIG_PATH.toFile().exists()) {
            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                try {
                    return GSON.fromJson(reader, MainConfig.class);
                } catch (Exception e) {
                    MainConfig config = new MainConfig();
                    config.save();
                    return config;
                }
            } catch (IOException e) {
                Main.LOGGER.info(e.getMessage());
            }
        }
        MainConfig config = new MainConfig();
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
