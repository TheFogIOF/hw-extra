package sk.thefogiof.hwextra.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import sk.thefogiof.hwextra.register.Configs;
import sk.thefogiof.hwextra.utils.TaskScheduler;

public class ReloadConfigCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hwextra")
                .then(ClientCommandManager.literal("reload")
                    .then(ClientCommandManager.literal("config")
                        .executes(ReloadConfigCommand::ReloadConfig)
                    )
                )
            );
        });
    }

    private static int ReloadConfig(CommandContext<FabricClientCommandSource> ctx) {
        Configs.reload();
        return 1;
    }
}
