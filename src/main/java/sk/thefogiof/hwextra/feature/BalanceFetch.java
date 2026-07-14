package sk.thefogiof.hwextra.feature;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import sk.thefogiof.hwextra.Main;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BalanceFetch {
    private static final Pattern BALANCE_PATTERN = Pattern.compile("(?i)Ваш\\s*баланс\\s*[:：]\\s*\\$?\\s*([\\d\\s.,]+)");
    private static CompletableFuture<String> pendingFuture = null;
    private static boolean eventRegistered = false;

    static {
        registerEvent();
    }

    private static void registerEvent() {
        if (eventRegistered) return;
        ClientReceiveMessageEvents.GAME.register(((message, overlay) -> {
            if (pendingFuture == null) return;
            String text = message.getString();
            Matcher matcher = BALANCE_PATTERN.matcher(text);
            if (matcher.find()) {
                String numberStr = matcher.group(1)
                        .replaceAll("[\\s\\u00A0\\u202F\\u2007]+", "")
                        .replaceAll("[^\\d.]", "")
                        .replace(',', '.');
                String number = numberStr.substring(0, numberStr.indexOf("."));
                try {
                    pendingFuture.complete(number);
                    pendingFuture = null;
                } catch (NumberFormatException e) {
                    pendingFuture.completeExceptionally(new RuntimeException("Неверный формат числа: " + numberStr, e));
                    pendingFuture = null;
                }
            }
        }));
        eventRegistered = true;
        Main.LOGGER.info("[BalanceFetch.class] Ивент зарегистрирован.");
    }

    /**
     * Запрашивает текущий баланс с сервера (плагин экономики).
     * @return CompletableFuture, который завершится значением баланса (double) или ошибкой.
     */
    public static CompletableFuture<String> requestBalance(Minecraft client) {
        if (pendingFuture != null && !pendingFuture.isDone()) {
            pendingFuture.cancel(true);
        }

        pendingFuture = new CompletableFuture<>();
        CompletableFuture<String> future = pendingFuture;

        if (client.player != null) {
            client.player.connection.sendCommand("money");
        } else {
            future.completeExceptionally(new IllegalStateException("Нет подключения"));
            pendingFuture = null;
            return future;
        }

        CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS).execute(() -> {
            if (!future.isDone()) {
                future.completeExceptionally(new RuntimeException("Таймаут получения баланса"));
                if (pendingFuture == future) pendingFuture = null;
            }
        });

        return future;
    }
}