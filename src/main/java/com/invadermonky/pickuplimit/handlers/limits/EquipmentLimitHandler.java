package com.invadermonky.pickuplimit.handlers.limits;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.handlers.CommonEventHandler;
import com.invadermonky.pickuplimit.handlers.util.EquipmentGroupCache;
import com.invadermonky.pickuplimit.limits.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EquipmentLimitHandler {

    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntityLiving().world;
        if(!world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if(player.isCreative() && !ConfigHandlerPL.pickup_limits.creativeOverride)
                return;

            THashMap<String, EquipmentGroupCache> limitGroups = new THashMap<>();

            //Baubles first
            if(ModIds.baubles.isLoaded()) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack baubleStack = handler.getStackInSlot(i);
                    for(EquipmentLimitGroup group : LimitRegistry.getEquipmentLimitGroups(player, baubleStack)) {
                        EquipmentGroupCache cachedGroup = createOrIncrementCache(player, baubleStack, limitGroups, group);
                        if(cachedGroup != null && cachedGroup.limit < cachedGroup.getInvCount()) {
                            //Baubles are removed fully from the slot instead of partial removal
                            ItemStack extracted = handler.extractItem(i, baubleStack.getCount(), false);
                            if(!player.addItemStackToInventory(extracted)) {
                                player.dropItem(extracted, true);
                            }
                            cachedGroup.shrinkInvCount(player, baubleStack);
                            CommonEventHandler.sendEquipmentLimitMessage(player, baubleStack, group);
                        }


                    }
                }
            }

            //Running through equipment slots.
            for(EntityEquipmentSlot equipmentSlot : EntityEquipmentSlot.values()) {
                //Excluding Mainhand as it is handled last and has a different logic from the other slots.
                if(equipmentSlot == EntityEquipmentSlot.MAINHAND)
                    continue;

                ItemStack stack = player.getItemStackFromSlot(equipmentSlot);
                for(EquipmentLimitGroup group : LimitRegistry.getEquipmentLimitGroups(player, stack)) {
                    if(!group.shouldCheckSlot(equipmentSlot))
                        continue;

                    EquipmentGroupCache cacheGroup = createOrIncrementCache(player, stack, limitGroups, group);
                    if(cacheGroup != null && cacheGroup.limit < cacheGroup.getInvCount()) {
                        //Equipment is removed fully from the slot instead of partial removal
                        player.setItemStackToSlot(equipmentSlot, ItemStack.EMPTY);
                        if(!player.addItemStackToInventory(stack)) {
                            player.dropItem(stack, true);
                        }
                        cacheGroup.shrinkInvCount(player, stack);
                        CommonEventHandler.sendEquipmentLimitMessage(player, stack, group);
                    }
                }
            }

            //Mainhand last. Dropping item if it exceeds the Equipment limit
            ItemStack mhStack = player.getHeldItemMainhand();
            for(EquipmentLimitGroup group : LimitRegistry.getEquipmentLimitGroups(player, mhStack)) {
                if(!group.shouldCheckSlot(EntityEquipmentSlot.MAINHAND))
                    continue;

                EquipmentGroupCache cachedGroup = createOrIncrementCache(player, mhStack, limitGroups, group);
                if(cachedGroup != null && cachedGroup.limit < cachedGroup.getInvCount()) {
                    //Mainhand equipment is dropped if held
                    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                    player.dropItem(mhStack, true);
                    cachedGroup.shrinkInvCount(player, mhStack);
                    CommonEventHandler.sendEquipmentLimitMessage(player, mhStack, group);
                }
            }
        }
    }

    private static EquipmentGroupCache createOrIncrementCache(EntityPlayer player, ItemStack stack, THashMap<String, EquipmentGroupCache> limitGroups, EquipmentLimitGroup group) {
        if(limitGroups.containsKey(group.getGroupName())) {
            limitGroups.get(group.getGroupName()).growInvCount(player, stack);
        } else {
            EquipmentGroupCache cache = new EquipmentGroupCache(player, group, stack);
            if(cache.limit >= 0) {
                limitGroups.put(group.getGroupName(), cache);
            }
        }
        return limitGroups.get(group.getGroupName());
    }
}
