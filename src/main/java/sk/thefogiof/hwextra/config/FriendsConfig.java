package sk.thefogiof.hwextra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import sk.thefogiof.hwextra.model.Friend;
import sk.thefogiof.hwextra.feature.Friends;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FriendsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hwextra/friends.json");

    public Map<String, String> friends = new HashMap<>();

    private static FriendsConfig instance;

    public static FriendsConfig getInstance() {
        if (instance == null) instance = load();

        Friends.friends.clear();
        instance.friends.forEach((name, apiKey) -> {
            Friends.friends.add(new Friend(name, apiKey));
        });

        return instance;
    }

    public static FriendsConfig reload() {
        instance = load();
        return instance;
    }

    private static FriendsConfig load() {
        if (CONFIG_PATH.toFile().exists()) {
            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                try {
                    return GSON.fromJson(reader, FriendsConfig.class);
                } catch (Exception e) {
                    FriendsConfig config = new FriendsConfig();
                    config.save();
                    return config;
                }
            } catch (IOException e) {
                Main.LOGGER.info(e.getMessage());
            }
        }
        FriendsConfig config = new FriendsConfig();
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
