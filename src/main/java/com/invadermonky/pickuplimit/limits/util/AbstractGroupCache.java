package com.invadermonky.pickuplimit.limits.util;

import com.invadermonky.pickuplimit.limits.api.ILimitGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class AbstractGroupCache<T extends AbstractLimitGroup<?>> implements ILimitGroup {
    protected final T group;
    protected final EntityPlayer player;
    protected final int limit;
    protected int invCount;

    public AbstractGroupCache(EntityPlayer player, T group, ItemStack stack) {
        this.player = player;
        this.group = group;
        this.limit = group.getLimit(player);
        this.invCount = this.getStackLimitValue(stack);
    }

    public AbstractGroupCache(EntityPlayer player, T group) {
        this(player, group, ItemStack.EMPTY);
    }

    @Override
    public String getName() {
        return this.group.getGroupName();
    }

    @Override
    public String getLimitMessage() {
        return this.group.getLimitMessage();
    }

    @Override
    public int getLimit() {
        return this.limit;
    }

    @Override
    public int getStackLimitValue(ItemStack stack) {
        if(this.group.getStackLimitFunction() != null) {
            return this.group.getStackLimitFunction().process(this.player, stack, this);
        }
        return this.group.getStackLimitValue(this.player, stack);
    }

    public boolean matches(ItemStack stack) {
        return this.group.matches(this.player, stack);
    }

    public void growInvCount(ItemStack stack) {
        this.invCount = Math.max(0, this.getInvCount() + this.getStackLimitValue(stack));
    }

    public void shrinkInvCount(ItemStack stack) {
        this.invCount = Math.max(0, this.getInvCount() - this.getStackLimitValue(stack));
    }

    public void setInvCount(int newCount) {
        this.invCount = newCount;
    }

    public int getInvCount() {
        return this.invCount;
    }
}
