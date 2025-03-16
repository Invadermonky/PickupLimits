package com.invadermonky.pickuplimit.limits.caches;

import com.invadermonky.pickuplimit.limits.groups.PickupLimitGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PickupGroupCache extends AbstractGroupCache<PickupLimitGroup> {
    public PickupGroupCache(EntityPlayer player, PickupLimitGroup group, ItemStack stack) {
        super(player, group, stack);
    }

    public PickupGroupCache(EntityPlayer player, PickupLimitGroup group) {
        super(player, group);
    }

    @Override
    public boolean handleLimitDrop(ItemStack stack, boolean dropItem) {
        return this.group.handleLimitDrop(this.player, stack, this, dropItem && this.shouldItemBeDropped());
    }

    public int getAdjustedPickupCount(int stackCount, int itemValue) {
        if(stackCount != itemValue) {
            return (int) Math.floor((double) stackCount / (double) itemValue * (double) (this.getLimit() - this.getInvCount()));
        }
        return stackCount;
    }
}
