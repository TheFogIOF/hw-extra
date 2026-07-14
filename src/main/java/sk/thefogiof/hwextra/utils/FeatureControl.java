package sk.thefogiof.hwextra.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.thefogiof.hwextra.network.FeatureControlPayload;
import sk.thefogiof.hwextra.network.packet.CheckFeaturesRequest;
import sk.thefogiof.hwextra.network.packet.CheckFeaturesResponse;
import sk.thefogiof.hwextra.network.packet.FeatureCheckerDeserializer;
import sk.thefogiof.hwextra.network.packet.FeatureCheckerSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FeatureControl {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureControl.class);

    private static final List<String> FEATURES = List.of("farmer_hud", "effect_hud", "friends");
    public static List<String> BLOCKLIST = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(CheckFeaturesRequest.class, new FeatureCheckerSerializer())
            .registerTypeAdapter(CheckFeaturesResponse.class, new FeatureCheckerDeserializer())
            .create();

    private static final ConcurrentHashMap<String, CompletableFuture<CheckFeaturesResponse>> pending = new ConcurrentHashMap<>();

    public static void register() {
        PayloadTypeRegistry.playC2S().register(FeatureControlPayload.FeatureControlPacket.TYPE, FeatureControlPayload.FeatureControlPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(FeatureControlPayload.FeatureControlPacket.TYPE, FeatureControlPayload.FeatureControlPacket.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(FeatureControlPayload.FeatureControlPacket.TYPE,
                (payload, context) -> {
                    String json = payload.json();
                    try {
                        CheckFeaturesResponse response = GSON.fromJson(json, CheckFeaturesResponse.class);
                        CompletableFuture<CheckFeaturesResponse> future = pending.remove(response.id());
                        if (future != null) {
                            future.complete(response);
                        }
                    } catch (Exception exception) {
                        LOGGER.info(exception.getMessage());
                    }
                }
        );

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (handler.getServerData() == null && !handler.getServerData().ip.contains("holyworld")) return;
            sendRequest()
                .thenAcceptAsync(response -> {
                    if (response.isOk()) {
                        LOGGER.info(response.toString());
                        BLOCKLIST.addAll(response.blocklist());
                    } else {
                        LOGGER.warn(response.error());
                    }
                })
                .exceptionally(ex -> {
                    LOGGER.warn(ex.getMessage());
                    return null;
                });
        });
    }

    public static CompletableFuture<CheckFeaturesResponse> sendRequest() {
        CheckFeaturesRequest request = new CheckFeaturesRequest("hwextra", FEATURES);
        CompletableFuture<CheckFeaturesResponse> future = new CompletableFuture<>();
        pending.put(request.id(), future);
        String json = GSON.toJson(request);
        FeatureControlPayload.FeatureControlPacket packet = new FeatureControlPayload.FeatureControlPacket(json);
        LOGGER.info(packet + " " + packet.type());
        ClientPlayNetworking.send(packet);
        return future
                .orTimeout(5, TimeUnit.SECONDS)
                .whenComplete((response, ex) -> pending.remove(request.id()));
    }
}
