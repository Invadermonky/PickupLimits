package com.invadermonky.pickuplimit.handlers;

import com.invadermonky.pickuplimit.PickupLimits;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = PickupLimits.MOD_ID)
public class ClientEventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        /* TODO: See how people like the mod and how they would like tooltips implemented
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();
        LimitRegistry.getEquipmentLimitGroups(player, stack).stream().filter(group -> );
        LimitRegistry.getPickupLimitGroups(player, stack);
         */
    }
}
