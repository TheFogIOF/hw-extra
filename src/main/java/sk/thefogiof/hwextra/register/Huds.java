package sk.thefogiof.hwextra.register;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import sk.thefogiof.hwextra.hud.*;
import sk.thefogiof.hwextra.utils.FeatureControl;

public class Huds {
    public static void register() {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("hwextra", "farmer_hud"), FarmerHud::render);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("hwextra", "friends_hud"), FriendsHud::render);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("hwextra", "effect_hud"), EffectHud::render);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("hwextra", "pos_hud"), PosHud::render);

        //HudElementRegistry.attachElementBefore(VanillaHudElements.MISC_OVERLAYS, Identifier.fromNamespaceAndPath("hw_extra", "rounded"), RoundedHud::render);
    }
}
