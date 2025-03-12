package com.invadermonky.pickuplimit.limits.handlers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.handlers.CommonEventHandler;
import com.invadermonky.pickuplimit.limits.PickupLimitGroup;
import com.invadermonky.pickuplimit.limits.util.PickupGroupCache;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import java.util.List;
import java.util.stream.Collectors;

public class PickupLimitHandler {

    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        EntityItem entityItem = event.getItem();
        ItemStack pickupStack = entityItem.getItem();
        World world = entityItem.world;
        EntityPlayer player = event.getEntityPlayer();

        if(player.isCreative() && !ConfigHandlerPL.pickup_limits.creativeOverride)
            return;

        if(!world.isRemote && !pickupStack.isEmpty()) {
            //Caching Limit Groups and their respective limits
            List<PickupGroupCache> limitGroups = LimitRegistry.getPickupLimitGroups(player, pickupStack).stream()
                    .map(group -> new PickupGroupCache(player, group))
                    .filter(cache -> cache.limit >= 0)
                    .collect(Collectors.toList());

            //Iterating over the player's inventory to get the current count
            for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack invStack = player.inventory.getStackInSlot(i);
                limitGroups.stream()
                        .filter(cache -> cache.group.matches(player, invStack))
                        .forEach(cache -> cache.growInvCount(player, invStack));
            }

            //Iterating over the player's bauble inventory to get the current count
            if(ModIds.baubles.isLoaded()) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack baubleStack = handler.getStackInSlot(i);
                    limitGroups.stream()
                            .filter(cache -> cache.group.matches(player, baubleStack))
                            .forEach(cache -> cache.growInvCount(player, baubleStack));
                }
            }

            //Getting the maximum allowable pickup amount and the controlling pickup group
            int maxPickup = pickupStack.getCount();
            PickupGroupCache controllingCache = null;
            for(PickupGroupCache cache : limitGroups) {
                if(maxPickup > cache.limit - cache.getInvCount()) {
                    maxPickup = cache.limit - cache.getInvCount();
                    controllingCache = cache;
                }
            }

            //Adding any allowed items to inventory and cancelling event if pickup limit is exceeded
            if(maxPickup <= 0) {
                event.setCanceled(true);
                CommonEventHandler.sendPickupLimitMessage(player, pickupStack, controllingCache);
            } else if(maxPickup < pickupStack.getCount()) {
                ItemStack pickup = pickupStack.splitStack(maxPickup);
                player.addItemStackToInventory(pickup);
                entityItem.setItem(pickupStack);
                event.setCanceled(true);
                CommonEventHandler.sendPickupLimitMessage(player, pickupStack, controllingCache);
            }
        }
    }

    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntityLiving().world;
        if(!world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if(player.isCreative() && !ConfigHandlerPL.pickup_limits.creativeOverride)
                return;

            //Used to hold values inventory amount totals
            THashMap<String, PickupGroupCache> limitGroups = new THashMap<>();

            //Checking baubles slots first to prevent unequipping if the inventory limit is exceeded
            if(ModIds.baubles.isLoaded()) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack baubleStack = handler.getStackInSlot(i);
                    if(baubleStack.isEmpty())
                        continue;

                    for(PickupLimitGroup group : LimitRegistry.getPickupLimitGroups(player, baubleStack)) {
                        PickupGroupCache cachedGroup = createOrIncrementCache(player, baubleStack, limitGroups, group);

                        //If inventory count exceeds pickup limit, drop the remainder items
                        if(cachedGroup != null && cachedGroup.limit < cachedGroup.getInvCount()) {
                            ItemStack extracted = handler.extractItem(i, cachedGroup.getInvCount() - cachedGroup.limit, false);
                            player.dropItem(extracted, true);
                            cachedGroup.setInvCount(cachedGroup.limit);
                            CommonEventHandler.sendPickupLimitMessage(player, baubleStack, cachedGroup);
                        }
                    }
                }
            }

            for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack invStack = player.inventory.getStackInSlot(i);
                if(invStack.isEmpty())
                    continue;

                //Iterating through existing pickup limit groups and adding stack count to running total
                for(PickupLimitGroup group : LimitRegistry.getPickupLimitGroups(player, invStack)) {
                    PickupGroupCache cachedGroup = createOrIncrementCache(player, invStack, limitGroups, group);

                    //If inventory count exceeds pickup limit, drop the remainder items
                    if(cachedGroup != null && cachedGroup.limit < cachedGroup.getInvCount()) {
                        ItemStack copy = invStack.copy();
                        ItemStack dropStack = copy.splitStack(cachedGroup.getInvCount() - cachedGroup.limit);
                        player.inventory.setInventorySlotContents(i, copy);
                        player.dropItem(dropStack, true);
                        cachedGroup.setInvCount(cachedGroup.limit);
                        CommonEventHandler.sendPickupLimitMessage(player, invStack, cachedGroup);
                    }
                }
            }
        }
    }

    private static PickupGroupCache createOrIncrementCache(EntityPlayer player, ItemStack stack, THashMap<String, PickupGroupCache> limitGroups, PickupLimitGroup group) {
        if(limitGroups.containsKey(group.getGroupName())) {
            limitGroups.get(group.getGroupName()).growInvCount(player, stack);
        } else {
            PickupGroupCache cache = new PickupGroupCache(player, group, stack);
            if(cache.limit >= 0) {
                limitGroups.put(group.getGroupName(), cache);
            }
        }
        return limitGroups.get(group.getGroupName());
    }
}
