package com.invadermonky.pickuplimit.handlers;

import com.invadermonky.pickuplimit.PickupLimit;
import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.config.ModTags;
import com.invadermonky.pickuplimit.limits.PickupLimitGroup;
import com.invadermonky.pickuplimit.limits.PickupLimitRegistry;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = PickupLimit.MOD_ID)
public class EventHandlerPL {
    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        EntityItem entityItem = event.getItem();
        ItemStack pickupStack = entityItem.getItem();
        World world = entityItem.world;
        EntityPlayer player = event.getEntityPlayer();

        if(player.isCreative() && !ConfigHandlerPL.pickup_limits.creativeOverride)
            return;

        if(!world.isRemote && !pickupStack.isEmpty()) {
            //Caching Limit Groups and their respective limits
            List<LimitGroupCache> limitGroups = PickupLimitRegistry.getPickupLimitGroups(player, pickupStack).stream()
                    .map(group -> new LimitGroupCache(player, group))
                    .filter(cache -> cache.limit >= 0)
                    .collect(Collectors.toList());

            //Iterating over the player's inventory to get the current
            for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack invStack = player.inventory.getStackInSlot(i);
                limitGroups.stream()
                        .filter(cache -> cache.group.matches(player, invStack))
                        .forEach(cache -> cache.growInvCount(invStack.getCount()));
            }

            //Getting the maximum allowable pickup amount and the controlling pickup group
            int maxPickup = pickupStack.getCount();
            PickupLimitGroup controllingGroup = PickupLimitGroup.EMPTY;
            for(LimitGroupCache cache : limitGroups) {
                if(maxPickup > cache.limit - cache.getInvCount()) {
                    maxPickup = cache.limit - cache.getInvCount();
                    controllingGroup = cache.group;
                }
            }

            //Adding any allowed items to inventory and cancelling event if pickup limit is exceeded
            if(maxPickup <= 0) {
                event.setCanceled(true);
                sendPickupLimitMessage(player, pickupStack, controllingGroup);
            } else if(maxPickup < pickupStack.getCount()) {
                ItemStack pickup = pickupStack.splitStack(maxPickup);
                player.addItemStackToInventory(pickup);
                entityItem.setItem(pickupStack);
                event.setCanceled(true);
                sendPickupLimitMessage(player, pickupStack, controllingGroup);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(ConfigHandlerPL.pickup_limits.inventoryCheckInterval == 0 || event.getEntityLiving().ticksExisted % ConfigHandlerPL.pickup_limits.inventoryCheckInterval != 0)
            return;

        World world = event.getEntityLiving().world;
        if(!world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if(player.isCreative() && !ConfigHandlerPL.pickup_limits.creativeOverride)
                return;

            //Used to hold values inventory amount totals
            THashMap<String,LimitGroupCache> limitGroups = new THashMap<>();
            for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack invStack = player.inventory.getStackInSlot(i);
                if(invStack.isEmpty())
                    continue;

                //Iterating through existing pickup limit groups and adding stack count to running total
                for(PickupLimitGroup group : PickupLimitRegistry.getPickupLimitGroups(player, invStack)) {
                    if(limitGroups.containsKey(group.getGroupName())) {
                        limitGroups.get(group.getGroupName()).growInvCount(invStack.getCount());
                    } else {
                        LimitGroupCache cache = new LimitGroupCache(player, group, invStack);
                        if(cache.limit >= 0) {
                            limitGroups.put(group.getGroupName(), cache);
                        }
                    }

                    //If inventory count exceeds pickup limit, drop the remainder items
                    LimitGroupCache cachedGroup = limitGroups.get(group.getGroupName());
                    if(cachedGroup != null && cachedGroup.limit < cachedGroup.invCount) {
                        ItemStack copy = invStack.copy();
                        ItemStack dropStack = copy.splitStack(cachedGroup.invCount - cachedGroup.limit);
                        player.inventory.setInventorySlotContents(i, copy);
                        player.dropItem(dropStack, true);
                        cachedGroup.setInvCount(cachedGroup.limit);
                        sendPickupLimitMessage(player, invStack, group);
                    }
                }
            }
            int x = 0;
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(ConfigHandlerPL.item_lifetimes.enable && event.getEntity() instanceof EntityItem) {
            ((EntityItem) event.getEntity()).lifespan = ModTags.getItemLifetime(((EntityItem) event.getEntity()).getItem());
        }
    }

    private static void sendPickupLimitMessage(EntityPlayer player, ItemStack pickupStack, PickupLimitGroup group) {
        if(ConfigHandlerPL.pickup_limits.enablePickupLimitMessage) {
            String limitMessage = group.getPickupMessage();
            ITextComponent text = new TextComponentTranslation(limitMessage, pickupStack.getDisplayName());
            if(text.getFormattedText().matches("^Format error: .*")) {
                player.sendStatusMessage(new TextComponentTranslation(limitMessage), true);
            } else {
                player.sendStatusMessage(text, true);
            }
        }
    }

    public static class LimitGroupCache {
        public final PickupLimitGroup group;
        public final int limit;
        private int invCount;

        public LimitGroupCache(EntityPlayer player, PickupLimitGroup group, ItemStack stack) {
            this.group = group;
            this.limit = group.getPickupLimit(player);
            this.invCount = stack.getCount();
        }

        public LimitGroupCache(EntityPlayer player, PickupLimitGroup group) {
            this.group = group;
            this.limit = group.getPickupLimit(player);
            this.invCount = 0;
        }

        public void growInvCount(int increment) {
            this.invCount += increment;
        }

        public void setInvCount(int newCount) {
            this.invCount = newCount;
        }

        public int getInvCount() {
            return this.invCount;
        }
    }
}
