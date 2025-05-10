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

    /**
     * Returns the number of items that can be picked up by the player taking into account the group limit and the current
     * inventory item count.
     */
    public int getAdjustedPickupCount(int stackCount, int itemValue) {
        return (int) (((this.getLimit() - this.getInvCount())) / ((double) stackCount / (double) itemValue));
    }
}
