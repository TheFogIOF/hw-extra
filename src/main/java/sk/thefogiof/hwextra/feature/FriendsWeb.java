package sk.thefogiof.hwextra.feature;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.model.Friend;
import sk.thefogiof.hwextra.register.Configs;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class FriendsWeb {
    private static final String SERVER_URL = Configs.mainConfig.apiEndPoint + "/friends/";
    private static final Gson GSON = new Gson();
    private static String username;
    private static HttpClient client;

    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((listener, sender, client) -> {
            if (client.player != null) {
                List<String> friendNames = Friends.friends.stream().map(friend -> friend.name).toList();
                if (listener.getServerData() != null) {
                    FriendsWeb.start(client.player.getName().getString(), friendNames, listener.getServerData().ip);
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> FriendsWeb.leave());

        ClientTickEvents.END_CLIENT_TICK.register(FriendsWeb::sendFriendWebRequest);
    }

    public static void start(String playerName, List<String> friendNames, String server) {
        username = playerName;
        client = HttpClient.newHttpClient();

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("server", server);
        json.add("friend_names", GSON.toJsonTree(friendNames));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(SERVER_URL + "join"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(FriendsWeb::handleResponse)
            .exceptionally(ex -> {
                Main.LOGGER.info(ex.getMessage());
                return null;
            });
    }

    private static void handleResponse(String response) {
        try {
            JsonObject obj = GSON.fromJson(response, JsonObject.class);
            if (obj.has("type") && "friend_update".equals(obj.get("type").getAsString())) {
                List<Friend> friends = GSON.fromJson(
                        obj.get("friends").getAsJsonArray(),
                        new com.google.gson.reflect.TypeToken<List<Friend>>(){}.getType()
                );
                Minecraft.getInstance().execute(() -> {
                    Friends.friends.clear();
                    Friends.friends.addAll(friends);
                });
            }
        } catch (Exception e) {
            Main.LOGGER.error("Failed to parse: {} {}", response, e);
        }
    }

    public static void sendRequest(String username, List<String> friendNames, String server) {
        if (client == null) return;

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("server", server);
        json.add("friend_names", GSON.toJsonTree(friendNames));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "get"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(FriendsWeb::handleResponse)
                .exceptionally(ex -> {
                    Main.LOGGER.info(ex.getMessage());
                    return null;
                });
    }

    public static void leave() {
        if (client == null) return;

        JsonObject json = new JsonObject();
        json.addProperty("username", username);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "leave"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(FriendsWeb::handleResponse)
                .exceptionally(ex -> {
                    Main.LOGGER.info(ex.getMessage());
                    return null;
                });
    }

    public static void sendFriendWebRequest(Minecraft client) {
        if (client.player != null) {
            String name = client.player.getName().getString();
            if (client.player.tickCount % 100 == 0) {
                List<String> friendNames = Friends.friends.stream().map(friend -> friend.name).toList();
                FriendsWeb.sendRequest(name, friendNames, getServer(client));
            }
        }
    }

    public static String getServer(Minecraft client) {
        String server = client.isSingleplayer() ? "singleplayer" : (client.getCurrentServer() != null ? client.getCurrentServer().ip : "unknown");
        if (client.getCurrentServer() != null) {
            try {
                if (client.gui.getTabList().header != null) {
                    String header = client.gui.getTabList().header.getString();
                    if (header.contains("Лобби")) server = "Лобби " + extractAnarchyNumber(header);
                    if (header.contains("Лайт")) server = "Лайт " + extractAnarchyNumber(header);
                    if (header.contains("Классик")) server = "Классик " + extractAnarchyNumber(header);
                }
            } catch (Exception e) {
                Main.LOGGER.info("Failed to get tab header.");
                return server;
            }
        }
        return server;
    }

    public static int extractAnarchyNumber(String header) {
        if (header == null) return -1;
        int hashPos = header.indexOf('#');
        if (hashPos == -1) return -1;
        int start = hashPos + 1;
        if (start >= header.length()) return -1;
        int end = start;
        while (end < header.length() && Character.isDigit(header.charAt(end))) end++;
        if (end == start) return -1;
        try {
            return Integer.parseInt(header.substring(start, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
