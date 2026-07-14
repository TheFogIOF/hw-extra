package sk.thefogiof.hwextra.network.packet;

import java.util.Collections;
import java.util.List;

public class CheckFeaturesResponse extends Response {

    private final List<String> blocklist;

    private CheckFeaturesResponse(String id, boolean ok, List<String> blocklist, String error) {
        super(id, ok, error);
        this.blocklist = blocklist;
    }

    public static CheckFeaturesResponse success(String id, List<String> blocklist) {
        return new CheckFeaturesResponse(id, true, Collections.unmodifiableList(blocklist), null);
    }

    public static CheckFeaturesResponse failure(String id, String error) {
        return new CheckFeaturesResponse(id, false, Collections.emptyList(), error);
    }

    public List<String> blocklist() {
        return blocklist;
    }

    @Override
    public String toString() {
        return isOk()
                ? "CheckFeaturesResponse{id='" + id() + "', ok=true, blocklist=" + blocklist + "}"
                : "CheckFeaturesResponse{id='" + id() + "', ok=false, error='" + error() + "'}";
    }
}
