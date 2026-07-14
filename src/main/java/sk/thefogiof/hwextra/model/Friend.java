package sk.thefogiof.hwextra.model;

public class Friend {
    public String name;
    public boolean online;
    public String server;
    public String secret;

    public Friend(String name) {
        this.name = name;
        this.online = false;
    }
}
