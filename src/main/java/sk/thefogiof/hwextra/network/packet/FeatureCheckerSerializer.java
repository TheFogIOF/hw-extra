package sk.thefogiof.hwextra.network.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public final class FeatureCheckerSerializer implements JsonSerializer<CheckFeaturesRequest> {

    @Override
    public JsonElement serialize(CheckFeaturesRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray features = new JsonArray();
        src.features().forEach(features::add);

        JsonObject payload = new JsonObject();
        payload.addProperty("client", src.client());
        payload.add("features", features);

        JsonObject envelope = new JsonObject();
        envelope.addProperty("id", src.id());
        envelope.addProperty("method", src.method());
        envelope.add("payload", payload);

        return envelope;
    }
}
