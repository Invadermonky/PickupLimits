package com.invadermonky.pickuplimit.handlers;

import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.config.ModTags;
import com.invadermonky.pickuplimit.limits.caches.AbstractGroupCache;
import com.invadermonky.pickuplimit.limits.handlers.EquipmentLimitHandler;
import com.invadermonky.pickuplimit.limits.handlers.PickupLimitHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = PickupLimits.MOD_ID)
public class CommonEventHandler {
    //Fires early to prevent bag mods from intercepting this pickup.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        PickupLimitHandler.onEntityItemPickup(event);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().ticksExisted % ConfigHandlerPL.pickup_limits.inventoryCheckInterval == 0) {
            EquipmentLimitHandler.onLivingUpdate(event);
            PickupLimitHandler.onLivingUpdate(event);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (ConfigHandlerPL.item_lifetimes.enable && event.getEntity() instanceof EntityItem) {
            ((EntityItem) event.getEntity()).lifespan = ModTags.getItemLifetime(((EntityItem) event.getEntity()).getItem());
        }
    }

    public static void sendLimitMessage(EntityPlayer player, ItemStack stack, @Nonnull AbstractGroupCache<?> groupCache, boolean shouldSend) {
        if (!shouldSend)
            return;

        String limitMessage = groupCache.getLimitMessage();
        ITextComponent text = new TextComponentTranslation(limitMessage, stack.getDisplayName());
        if (text.getFormattedText().matches("^Format error: .*")) {
            text = new TextComponentTranslation(limitMessage);
        }
        player.sendStatusMessage(text, true);
    }
}
