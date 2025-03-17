package com.invadermonky.pickuplimit.config;

import com.invadermonky.pickuplimit.PickupLimits;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = PickupLimits.MOD_ID)
public class ConfigHandlerPL {
    public static ConfigPickupLimits pickup_limits = new ConfigPickupLimits();
    public static ConfigItemLifetimes item_lifetimes = new ConfigItemLifetimes();

    public static class ConfigPickupLimits {
        @Config.Comment("Enabling this feature allows the item limit logic to run on players in creative mode.")
        public boolean creativeOverride = false;

        @Config.Comment("Enables sending a message to the player when they are unable to pick up an item due to pickup limits.")
        public boolean enablePickupLimitMessage = true;

        @Config.Comment("Enables sending a message to the player when they are unable to equip an item due to equipment limits.")
        public boolean enableEquipmentLimitMessage = true;

        @Config.Comment("Enables limit tooltips on items. Tooltips must be defined in limit groups builders using the 'setLimitTooltip' method.")
        public boolean enableTooltipLimitMessage = true;

        @Config.RangeInt(min = 1, max = 12000)
        @Config.Comment("The time, in ticks, between each inventory check. The inventory check will scan a player's inventory to ensure\n" +
                "they have not exceeded any item limits.")
        public int inventoryCheckInterval = 20;
    }

    public static class ConfigItemLifetimes {
        @Config.Comment("Enables the item entity lifetime features of this mod.")
        public boolean enable = false;

        @Config.RangeInt(min = 0, max = Short.MAX_VALUE)
        @Config.Comment("Sets the global item lifetime before dropped items despawn (in ticks).")
        public int globalItemLifetime = 6000;

        @Config.Comment("Individual item lifetime overrides. A lifetime of -32768 will prevent the item from despawning.\n" +
                "Format: modid:itemid:[meta]{nbtdata}=lifetime (meta and nbtdata are optional)\n" +
                "Examples:\n" +
                "  minecraft:stone=20\n" +
                "  minecraft:stone:0=20\n" +
                "  minecraft:stone{\"tagkey\":\"tagdata\"}=20\n" +
                "  minecraft:stone:0{\"tagkey\":\"tagdata\"}=20")
        public String[] itemLifetimeOverrides = {};
    }

    @Mod.EventBusSubscriber(modid = PickupLimits.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(PickupLimits.MOD_ID)) {
                ConfigManager.sync(PickupLimits.MOD_ID, Config.Type.INSTANCE);
                ModTags.syncConfig();
            }
        }
    }
}
