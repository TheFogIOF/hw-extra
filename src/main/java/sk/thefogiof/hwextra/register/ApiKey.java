package sk.thefogiof.hwextra.register;

import net.fabricmc.loader.api.FabricLoader;
import sk.thefogiof.hwextra.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class ApiKey {
    private static String key = "";

    public static String getApiKey() {
        return key;
    }

    public static String generateApiKey() {
        long unixSeconds = Instant.now().getEpochSecond();
        int randomPart = (int) (Math.random() * Integer.MAX_VALUE);
        key = Long.toHexString(unixSeconds) + Integer.toHexString(randomPart);
        save();
        return key;
    }

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hwextra/.api-key");

    public static void register() {
        if (CONFIG_PATH.toFile().exists()) {
            try {
                key = Files.readString(CONFIG_PATH);
            } catch (IOException e) {
                Main.LOGGER.warn(e.getMessage());
            }
        }
        else {
            generateApiKey();
        }
    }

    private static void save() {
        try {
            Path parent = CONFIG_PATH.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.writeString(CONFIG_PATH, key);
        } catch (IOException e) {
            Main.LOGGER.warn(e.getMessage());
        }
    }
}