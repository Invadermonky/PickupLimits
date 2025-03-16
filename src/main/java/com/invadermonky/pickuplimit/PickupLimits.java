package com.invadermonky.pickuplimit;

import com.invadermonky.pickuplimit.compat.crafttweaker.ModIntegrationCT;
import com.invadermonky.pickuplimit.config.ModTags;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = PickupLimits.MOD_ID,
        name = PickupLimits.MOD_NAME,
        version = PickupLimits.VERSION,
        acceptedMinecraftVersions = PickupLimits.MC_VERSION
)
public class PickupLimits {
    public static final String MOD_ID = "pickuplimits";
    public static final String MOD_NAME = "Pickup Limits";
    public static final String VERSION = "1.1.0";
    public static final String MC_VERSION = "[1.12.2]";

    @Mod.Instance(MOD_ID)
    public static PickupLimits instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if(ModIds.crafttweaker.isLoaded()) MinecraftForge.EVENT_BUS.register(new ModIntegrationCT());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModTags.syncConfig();
    }
}
