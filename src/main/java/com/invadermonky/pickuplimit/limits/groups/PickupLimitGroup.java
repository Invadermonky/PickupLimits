package com.invadermonky.pickuplimit.limits.groups;

import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.handlers.CommonEventHandler;
import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.limits.caches.AbstractGroupCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.oredict.OreDictionary;

public class PickupLimitGroup extends AbstractLimitGroup<PickupLimitBuilder> {
    public static final PickupLimitGroup EMPTY = new PickupLimitBuilder(null, -1).build();

    public PickupLimitGroup(PickupLimitBuilder builder) {
        super(builder);
    }

    @Override
    public int getStackLimitValue(EntityPlayer player, ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public boolean matches(EntityPlayer player, ItemStack stack) {
        //Values of -1 means the limit is not active.
        if(!stack.isEmpty() && this.getLimitWithStageOverride(player) != -1) {
            for (ItemStack limitStack : this.getLimitStacks(player)) {
                if (OreDictionary.itemMatches(limitStack, stack, false)) {
                    if (!limitStack.hasTagCompound() || ItemStack.areItemStackTagsEqual(stack, limitStack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean handleLimitDrop(EntityPlayer player, ItemStack stack, AbstractGroupCache<?> groupCache, boolean dropItem) {
        CommonEventHandler.sendLimitMessage(player, stack, groupCache, ConfigHandlerPL.pickup_limits.enablePickupLimitMessage);
        if (!this.encumberedEffects.isEmpty()) {
            final int duration = ConfigHandlerPL.pickup_limits.inventoryCheckInterval + 20;
            this.encumberedEffects.forEach((potion, amplifier) -> player.addPotionEffect(new PotionEffect(potion, duration, amplifier, true, false)));
        } else if(dropItem) {
            player.dropItem(stack, true);
            groupCache.shrinkInvCount(stack);
            return true;
        }
        return false;
    }
}
