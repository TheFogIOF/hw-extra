package sk.thefogiof.hwextra.register;

import sk.thefogiof.hwextra.config.FriendsConfig;
import sk.thefogiof.hwextra.config.MainConfig;
import sk.thefogiof.hwextra.config.VarConfig;

public class Configs {
    public static VarConfig varConfig;
    public static FriendsConfig friendsConfig;
    public static MainConfig mainConfig;

    private static String HudMoney = "";

    public static void register() {
        varConfig = VarConfig.getInstance();
        friendsConfig = FriendsConfig.getInstance();
        mainConfig = MainConfig.getInstance();
    }

    public static void reload() {
        varConfig = VarConfig.reload();
        friendsConfig = FriendsConfig.reload();
        mainConfig = MainConfig.reload();
    }

    public static String getHudMoney() {
        return HudMoney;
    }

    public static void setHudMoney(String hudMoney) {
        HudMoney = hudMoney;
    }

    public static Long getBuyerExp() {
        return varConfig.buyerExperience;
    }

    public static void setBuyerExp(Long buyerExp) {
        varConfig.buyerExperience = buyerExp;
    }
}
