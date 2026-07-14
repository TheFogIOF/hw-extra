package sk.thefogiof.hwextra.network.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class FeatureCheckerDeserializer implements JsonDeserializer<CheckFeaturesResponse> {

    @Override
    public CheckFeaturesResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            JsonObject root = json.getAsJsonObject();

            JsonElement idElement = root.get("id");
            String id = (idElement != null && !idElement.isJsonNull()) ? idElement.getAsString() : null;

            JsonElement okElement = root.get("ok");
            if (okElement == null || okElement.isJsonNull()) {
                throw new JsonParseException("missing 'ok' field");
            }

            if (!okElement.getAsBoolean()) {
                JsonElement errorElement = root.get("error");
                String error = (errorElement != null && !errorElement.isJsonNull())
                        ? errorElement.getAsString()
                        : "unknown error";
                return CheckFeaturesResponse.failure(id, error);
            }

            JsonElement payloadElement = root.get("payload");
            if (payloadElement == null || payloadElement.isJsonNull()) {
                throw new JsonParseException("missing 'payload' field");
            }

            JsonElement blocklistElement = payloadElement.getAsJsonObject().get("blocklist");
            if (blocklistElement == null || blocklistElement.isJsonNull()) {
                throw new JsonParseException("missing 'blocklist' field");
            }

            JsonArray arr = blocklistElement.getAsJsonArray();
            List<String> blocklist = new ArrayList<>();
            arr.forEach(el -> blocklist.add(el.getAsString()));

            return CheckFeaturesResponse.success(id, blocklist);

        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException("failed to parse response: " + e.getMessage(), e);
        }
    }
}