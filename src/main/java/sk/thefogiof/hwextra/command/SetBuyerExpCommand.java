package sk.thefogiof.hwextra.command;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.register.Configs;

public class SetBuyerExpCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hwextra")
                .then(ClientCommandManager.literal("set_buyer_experience")
                    .then(ClientCommandManager.argument("value", LongArgumentType.longArg())
                        .executes(SetBuyerExpCommand::SetBuyerExperience)
                    )
                )
            );
        });
    }

    private static int SetBuyerExperience(CommandContext<FabricClientCommandSource> ctx) {
        long value = LongArgumentType.getLong(ctx, "value");
        Configs.setBuyerExp(value);
        Configs.varConfig.save();
        return 1;
    }
}
