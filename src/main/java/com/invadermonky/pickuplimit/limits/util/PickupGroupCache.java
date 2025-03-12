package com.invadermonky.pickuplimit.limits.util;

import com.invadermonky.pickuplimit.limits.PickupLimitGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PickupGroupCache extends AbstractGroupCache<PickupLimitGroup> {
    public PickupGroupCache(EntityPlayer player, PickupLimitGroup group, ItemStack stack) {
        super(player, group, stack);
    }

    public PickupGroupCache(EntityPlayer player, PickupLimitGroup group) {
        super(player, group);
    }
}
