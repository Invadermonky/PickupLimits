package com.invadermonky.pickuplimit.limits.handlers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.limits.caches.EquipmentGroupCache;
import com.invadermonky.pickuplimit.limits.groups.EquipmentLimitGroup;
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
    //TODO: Do not unequip items when there are encumbered effects

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
                        EquipmentGroupCache groupCache = createOrIncrementCache(player, baubleStack, limitGroups, group);
                        if(groupCache != null && groupCache.getLimit() < groupCache.getInvCount()) {
                            //Baubles uses an IItemHandler so removal needs to be handled through the extract method
                            ItemStack extracted = handler.extractItem(i, baubleStack.getCount(), false);
                            groupCache.handleLimitDrop(extracted, false);
                        }
                    }
                }
            }

            //Iterating through equipment slots backwards
            for(int i = EntityEquipmentSlot.values().length - 1; i >= 0; i--) {
                EntityEquipmentSlot equipmentSlot = EntityEquipmentSlot.values()[i];
                //Excluding Mainhand as it is handled last and has a different logic than the other slots
                if(equipmentSlot == EntityEquipmentSlot.MAINHAND)
                    continue;

                ItemStack stack = player.getItemStackFromSlot(equipmentSlot);
                for(EquipmentLimitGroup group : LimitRegistry.getEquipmentLimitGroups(player, stack)) {
                    if(!group.shouldCheckSlot(equipmentSlot))
                        continue;

                    EquipmentGroupCache groupCache = createOrIncrementCache(player, stack, limitGroups, group);
                    if(groupCache != null && groupCache.getLimit() < groupCache.getInvCount()) {
                        //Equipment is removed fully from the slot instead of partial removal
                        groupCache.handleLimitDrop(stack, false);
                        player.setItemStackToSlot(equipmentSlot, ItemStack.EMPTY);
                    }
                }
            }

            //Mainhand last. Dropping item if it exceeds the Equipment limit
            ItemStack mhStack = player.getHeldItemMainhand();
            for(EquipmentLimitGroup group : LimitRegistry.getEquipmentLimitGroups(player, mhStack)) {
                if(!group.shouldCheckSlot(EntityEquipmentSlot.MAINHAND))
                    continue;

                EquipmentGroupCache groupCache = createOrIncrementCache(player, mhStack, limitGroups, group);
                if(groupCache != null && groupCache.getLimit() < groupCache.getInvCount()) {
                    //Mainhand equipment is dropped if held
                    groupCache.handleLimitDrop(mhStack, true);
                    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                }
            }
        }
    }
    
    private static EquipmentGroupCache createOrIncrementCache(EntityPlayer player, ItemStack stack, THashMap<String, EquipmentGroupCache> limitGroups, EquipmentLimitGroup group) {
        if(limitGroups.containsKey(group.getGroupName())) {
            limitGroups.get(group.getGroupName()).growInvCount( stack);
        } else {
            EquipmentGroupCache cache = new EquipmentGroupCache(player, group, stack);
            if(cache.getLimit() >= 0) {
                limitGroups.put(group.getGroupName(), cache);
            }
        }
        return limitGroups.get(group.getGroupName());
    }
}
