package sk.thefogiof.hwextra.register;

import sk.thefogiof.hwextra.command.*;

public class Commands {
    public static void register() {
        FriendCommand.register();
        SelectServerCommand.register();
        SetBuyerExpCommand.register();
        ReloadConfigCommand.register();
        ApiKeyCommand.register();
    }
}
