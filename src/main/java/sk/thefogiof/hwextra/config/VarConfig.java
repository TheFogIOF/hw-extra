package sk.thefogiof.hwextra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class VarConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hwextra/var.json");

    public long buyerExperience = 0;

    private static VarConfig instance;

    public static VarConfig getInstance() {
        if (instance == null) instance = load();
        return instance;
    }

    public static VarConfig reload() {
        instance = load();
        return instance;
    }

    private static VarConfig load() {
        if (CONFIG_PATH.toFile().exists()) {
            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                try {
                    return GSON.fromJson(reader, VarConfig.class);
                } catch (Exception e) {
                    VarConfig config = new VarConfig();
                    config.save();
                    return config;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        VarConfig config = new VarConfig();
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
            e.printStackTrace();
        }
    }
}
