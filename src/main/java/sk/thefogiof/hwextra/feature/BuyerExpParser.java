package sk.thefogiof.hwextra.feature;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import sk.thefogiof.hwextra.Main;
import sk.thefogiof.hwextra.register.Configs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyerExpParser {
    private static final Pattern SALE_PATTERN = Pattern.compile(
            "(?i)\\s*С вашего баланса списано\\s*([\\d\\s.]+)\\s*очков скупщика"
    );
    private static boolean eventRegistered = false;
    public static void registerEvent() {
        if (eventRegistered) return;
        ClientReceiveMessageEvents.GAME.register(((message, overlay) -> {
            String raw = message.getString();
            Matcher matcher = SALE_PATTERN.matcher(raw);
            if (matcher.find()) {
                try {
                    int points = Integer.parseInt(matcher.group(1).replaceAll("[\\s\\u00A0\\u202F\\u2007]+", ""));
                    Configs.setBuyerExp(Configs.getBuyerExp() - points);
                    Configs.varConfig.save();
                    //Main.LOGGER.info("Распарсена продажа x{} предметов: монет={}, очков={}", items, coins, points);
                } catch (NumberFormatException e) {
                    Main.LOGGER.error("Ошибка парсинга чисел", e);
                }
            }
        }));
        eventRegistered = true;
        Main.LOGGER.info("[BuyerExpParser.class] Ивент зарегистрирован.");
    }
}