package sk.thefogiof.hwextra.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import sk.thefogiof.hwextra.feature.FriendsWeb;
import sk.thefogiof.hwextra.model.Friend;
import sk.thefogiof.hwextra.register.Configs;
import sk.thefogiof.hwextra.feature.Friends;

import java.util.Objects;

public class FriendCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hwextra")
                .then(ClientCommandManager.literal("friend")
                    .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("value", StringArgumentType.string())
                            .executes(FriendCommand::AddFriend)
                        )
                    )
                    .then(ClientCommandManager.literal("remove")
                        .then(ClientCommandManager.argument("value", StringArgumentType.string())
                            .executes(FriendCommand::RemoveFriend)
                        )
                    )
                )
            );
        });
    }

    private static int AddFriend(CommandContext<FabricClientCommandSource> ctx) {
        String name = StringArgumentType.getString(ctx, "value");
        Configs.friendsConfig.friends.put(name, "");
        Configs.friendsConfig.save();
        Friends.friends.add(new Friend(name));
        FriendsWeb.sendFriendWebRequest(ctx.getSource().getClient());
        return 1;
    }

    private static int RemoveFriend(CommandContext<FabricClientCommandSource> ctx) {
        String name = StringArgumentType.getString(ctx, "value");
        Configs.friendsConfig.friends.remove(name);
        Configs.friendsConfig.save();
        Friends.friends.forEach(friend -> {
            if (Objects.equals(friend.name, name)) Friends.friends.remove(friend);
        });
        FriendsWeb.sendFriendWebRequest(ctx.getSource().getClient());
        return 1;
    }
}
