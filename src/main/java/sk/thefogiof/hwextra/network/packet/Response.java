package sk.thefogiof.hwextra.network.packet;

public abstract class Response {

    private final String id;
    private final boolean ok;
    private final String error;

    protected Response(String id, boolean ok, String error) {
        this.id = id;
        this.ok = ok;
        this.error = error;
    }

    public String id() {
        return id;
    }

    public boolean isOk() {
        return ok;
    }

    public String error() {
        return error;
    }
}