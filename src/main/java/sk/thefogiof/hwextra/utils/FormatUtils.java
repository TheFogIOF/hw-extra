package sk.thefogiof.hwextra.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class FormatUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatUtils.class);

    public static String formatNumber(String numStr) {
        if (numStr.isBlank()) return numStr;
        try {
            long number = Long.parseLong(numStr);
            boolean negative = number < 0;
            if (negative) number = -number;
            String suffix = "";
            double divisor = 1.0;
            if (number >= 1_000_000_000_000L) {
                divisor = 1_000_000_000_000.0;
                suffix = "t";
            } else if (number >= 1_000_000_000L) {
                divisor = 1_000_000_000.0;
                suffix = "b";
            } else if (number >= 1_000_000L) {
                divisor = 1_000_000.0;
                suffix = "m";
            } else if (number >= 1_000L) {
                divisor = 1_000.0;
                suffix = "k";
            } else return negative ? "-" + numStr : numStr;
            double value = number / divisor;
            double floored = Math.floor(value * 10.0) / 10.0;
            String formatted = String.format("%.1f", floored).replace(',','.');
            if (formatted.endsWith(".0")) formatted = formatted.substring(0, formatted.length() - 2);
            return (negative ? "-" : "") + formatted + suffix;
        } catch (Exception e) {
            LOGGER.error("Ошибка парсинга чисел", e);
        }
        return numStr;
    }
}
