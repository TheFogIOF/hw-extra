package sk.thefogiof.hwextra.model;

public class Friend {
    public String name;
    public boolean online;
    public String server;
    public String secret;

    public Friend(String name) {
        this.name = name;
        this.secret = "";
        this.online = false;
    }

    public Friend(String name, String apiKey) {
        this.name = name;
        this.secret = apiKey;
        this.online = false;
    }
}
