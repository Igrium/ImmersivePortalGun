package com.igrium.imm_pgun;

import com.igrium.imm_pgun.entity.NullPortalEntity;
import com.igrium.imm_pgun.entity.PlacedPortalEntity;
import com.igrium.imm_pgun.test.SpawnPortalCommand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class ImmersivePortalGunMod implements ModInitializer {

    public static enum PortalColor {
        BLUE(ColorHelper.Argb.getArgb(255, 23, 100, 255)),
        ORANGE(ColorHelper.Argb.getArgb(255, 255, 107, 15));

        private final int color;
        private PortalColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

    public static final PortalGunItem PORTAL_GUN_ITEM = new PortalGunItem(new FabricItemSettings().maxCount(1));

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("immersive_portalgun", "portalgun"), PORTAL_GUN_ITEM);

        Registry.register(Registries.ENTITY_TYPE, new Identifier("immersive_portalgun", "placed_portal"), PlacedPortalEntity.TYPE);
        Registry.register(Registries.ENTITY_TYPE, new Identifier("immersive_portalgun", "null_portal"), NullPortalEntity.TYPE);

        CommandRegistrationCallback.EVENT.register(SpawnPortalCommand::register);
    }
}