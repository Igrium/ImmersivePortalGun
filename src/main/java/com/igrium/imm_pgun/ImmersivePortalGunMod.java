package com.igrium.imm_pgun;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ImmersivePortalGunMod implements ModInitializer {

    public static enum PortalColor {
        BLUE,
        ORANGE
    }

    public static final PortalGunItem PORTAL_GUN_ITEM = new PortalGunItem(new FabricItemSettings().maxCount(1));

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("immersive_portalgun", "portalgun"), PORTAL_GUN_ITEM);
    }
}