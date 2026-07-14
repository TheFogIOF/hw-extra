package sk.thefogiof.hwextra.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.utils.FormatUtils;
import sk.thefogiof.hwextra.utils.TaskScheduler;

public class SelectServerCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hwextra")
                .then(ClientCommandManager.literal("server_lite")
                    .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(1,31))
                        .executes(SelectServerCommand::SelectServer)
                    )
                )
            );
        });
    }

    private static int[] transformSlot(int n) {
        if (n >= 1 && n <= 15) return new int[]{0, 18 + (n - 1)};
        else if (n >= 16 && n <= 31) return new int[]{1, 18 + (n - 16)};
        else throw new IllegalArgumentException("Число должно быть от 1 до 31, получено: " + n);
    }

    private static int SelectServer(CommandContext<FabricClientCommandSource> ctx) {
        int serverNumber = IntegerArgumentType.getInteger(ctx, "value");
        int[] serv = transformSlot(serverNumber);
        ctx.getSource().getPlayer().connection.sendCommand("lite");
        TaskScheduler.schedule(5, () -> {
            var handler = ctx.getSource().getPlayer().containerMenu;
            Int2ObjectMap<HashedStack> changedSlots = new Int2ObjectOpenHashMap<>();
            HashedStack carried = HashedStack.EMPTY;
            ctx.getSource().getPlayer().connection.send(new ServerboundContainerClickPacket(
                    handler.containerId,
                    handler.getStateId(),
                    (short) serv[0],
                    (byte) 0,
                    ClickType.PICKUP,
                    changedSlots,
                    carried
            ));
        });
        TaskScheduler.schedule(10, () -> {
            var handler = ctx.getSource().getPlayer().containerMenu;
            Int2ObjectMap<HashedStack> changedSlots = new Int2ObjectOpenHashMap<>();
            HashedStack carried = HashedStack.EMPTY;
            ctx.getSource().getPlayer().connection.send(new ServerboundContainerClickPacket(
                    handler.containerId,
                    handler.getStateId(),
                    (short) serv[1],
                    (byte) 0,
                    ClickType.PICKUP,
                    changedSlots,
                    carried
            ));
        });
        return 1;
    }
}
