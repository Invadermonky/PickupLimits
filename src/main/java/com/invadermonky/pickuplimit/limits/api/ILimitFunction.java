package com.invadermonky.pickuplimit.limits.api;

import com.invadermonky.pickuplimit.limits.util.AbstractGroupCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ILimitFunction {
    int process(EntityPlayer player, ItemStack stack, AbstractGroupCache<?> group);
}
