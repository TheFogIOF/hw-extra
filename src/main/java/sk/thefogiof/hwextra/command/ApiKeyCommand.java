package sk.thefogiof.hwextra.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import sk.thefogiof.hwextra.register.ApiKey;

public class ApiKeyCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hwextra")
                .then(ClientCommandManager.literal("api")
                    .then(ClientCommandManager.literal("new-key")
                        .executes(ApiKeyCommand::NewKey)
                    )
                    .then(ClientCommandManager.literal("get-key")
                        .executes(ApiKeyCommand::GetKey)
                    )
                )
            );
        });
    }

    private static int NewKey(CommandContext<FabricClientCommandSource> ctx) {
        ApiKey.generateApiKey();
        return 1;
    }

    private static int GetKey(CommandContext<FabricClientCommandSource> ctx) {
        Component message = Component.literal("Нажми сюда, чтобы скопировать api ключ.")
                .withStyle(style -> style
                        .withClickEvent(new ClickEvent.CopyToClipboard(ApiKey.getApiKey()))
                ).withColor(0x945239);

        ctx.getSource().getPlayer().displayClientMessage(message, false);
        return 1;
    }
}
