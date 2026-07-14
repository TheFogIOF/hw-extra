package sk.thefogiof.hwextra.feature;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.register.Configs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoSaleParser {
    private static final Pattern SALE_PATTERN = Pattern.compile(
            "(?i)(\\d+)\\s*предм\\.?\\s*продано\\s*за\\s*([\\d\\s,]+)\\s*монет\\s*¤\\s*и\\s*\\+\\s*([\\d\\s,]+)\\s*очков"
    );
    private static boolean eventRegistered = false;
    public static void registerEvent() {
        if (eventRegistered) return;
        ClientReceiveMessageEvents.GAME.register(((message, overlay) -> {
            String raw = message.getString();
            Matcher matcher = SALE_PATTERN.matcher(raw);
            if (matcher.find()) {
                try {
                    int items = Integer.parseInt(matcher.group(1).replaceAll("[\\s\\u00A0\\u202F\\u2007]+", ""));
                    int coins = Integer.parseInt(matcher.group(2).replaceAll("[\\s\\u00A0\\u202F\\u2007]+", ""));
                    int points = Integer.parseInt(matcher.group(3).replaceAll("[\\s\\u00A0\\u202F\\u2007]+", ""));
                    BalanceFetch.requestBalance(Minecraft.getInstance()).thenAccept(Configs::setHudMoney);
                    Configs.setBuyerExp(Configs.getBuyerExp() + points);
                    Configs.varConfig.save();
                    //Main.LOGGER.info("Распарсена продажа x{} предметов: монет={}, очков={}", items, coins, points);
                } catch (NumberFormatException e) {
                    Main.LOGGER.error("Ошибка парсинга чисел", e);
                }
            }
        }));
        eventRegistered = true;
        Main.LOGGER.info("[AutoSaleParser.class] Ивент зарегистрирован.");
    }
}