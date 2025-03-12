package com.invadermonky.pickuplimit.compat.crafttweaker;

import com.invadermonky.pickuplimit.registry.LimitRegistry;
import crafttweaker.mc1120.events.ScriptRunEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModIntegrationCT {
    @SubscribeEvent
    public void onScriptReloading(ScriptRunEvent.Pre event) {
        LimitRegistry.removeAllEquipmentLimitGroups();
        LimitRegistry.removeAllPickupLimitGroups();
    }
}
