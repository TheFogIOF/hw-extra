package sk.thefogiof.hwextra.network.packet;

import java.util.List;

public final class CheckFeaturesRequest extends Request {

    private final String client;
    private final List<String> features;

    public CheckFeaturesRequest(String client, List<String> features) {
        super("checkFeatures");
        this.client = client;
        this.features = features;
    }

    public String client() {
        return client;
    }

    public List<String> features() {
        return features;
    }
}