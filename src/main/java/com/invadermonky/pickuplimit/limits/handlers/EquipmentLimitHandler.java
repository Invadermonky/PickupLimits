package com.invadermonky.pickuplimit.limits.handlers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.handlers.CommonEventHandler;
import com.invadermonky.pickuplimit.limits.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.limits.util.EquipmentGroupCache;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
                        EquipmentGroupCache groupCache = createOrIncrementCache(player, baubleStack, limitGroups, group);
                        if(groupCache != null && groupCache.limit < groupCache.getInvCount()) {
                            //Baubles uses an IItemHandler so removal needs to be handled through the extract method
                            ItemStack extracted = handler.extractItem(i, baubleStack.getCount(), false);
                            handleEquipmentDrop(player, extracted, groupCache, false);
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
                    if(groupCache != null && groupCache.limit < groupCache.getInvCount()) {
                        //Equipment is removed fully from the slot instead of partial removal
                        handleEquipmentDrop(player, stack, groupCache, false);
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
                if(groupCache != null && groupCache.limit < groupCache.getInvCount()) {
                    //Mainhand equipment is dropped if held
                    handleEquipmentDrop(player, mhStack, groupCache, true);
                    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                }
            }
        }
    }
    
    private static void handleEquipmentDrop(EntityPlayer player, ItemStack stack, EquipmentGroupCache groupCache, boolean forceDrop) {
        player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0f, 1.0f);
        CommonEventHandler.sendEquipmentLimitMessage(player, stack, groupCache);
        if(forceDrop || !player.addItemStackToInventory(stack)) {
            player.dropItem(stack, true);
        }
        groupCache.shrinkInvCount(player, stack);
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
