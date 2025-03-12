package com.invadermonky.pickuplimit;

import com.invadermonky.pickuplimit.compat.crafttweaker.ModIntegrationCT;
import com.invadermonky.pickuplimit.config.ModTags;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = PickupLimit.MOD_ID,
        name = PickupLimit.MOD_NAME,
        version = PickupLimit.VERSION,
        acceptedMinecraftVersions = PickupLimit.MC_VERSION
)
public class PickupLimit {
    public static final String MOD_ID = "pickuplimit";
    public static final String MOD_NAME = "Pickup Limit";
    public static final String VERSION = "1.0.0";
    public static final String MC_VERSION = "[1.12.2]";

    @Mod.Instance(MOD_ID)
    public static PickupLimit instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if(ModIds.crafttweaker.isLoaded()) MinecraftForge.EVENT_BUS.register(new ModIntegrationCT());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModTags.syncConfig();
    }
}
