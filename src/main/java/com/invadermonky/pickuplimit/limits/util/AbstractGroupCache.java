package com.invadermonky.pickuplimit.limits.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class AbstractGroupCache<T extends AbstractLimitGroup<?>>{
    public final T group;
    public final int limit;
    protected int invCount;

    public AbstractGroupCache(EntityPlayer player, T group, ItemStack stack) {
        this.group = group;
        this.limit = group.getLimit(player);
        this.invCount = group.getStackLimitValue(player, stack);
    }

    public AbstractGroupCache(EntityPlayer player, T group) {
        this(player, group, ItemStack.EMPTY);
    }

    public void growInvCount(EntityPlayer player, ItemStack stack) {
        this.invCount += this.group.getStackLimitValue(player, stack);
    }

    public void shrinkInvCount(EntityPlayer player, ItemStack stack) {
        this.invCount = Math.max(0, this.getInvCount() - this.group.getStackLimitValue(player, stack));
    }

    public void setInvCount(int newCount) {
        this.invCount = newCount;
    }

    public int getInvCount() {
        return this.invCount;
    }
}
