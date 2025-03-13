package com.invadermonky.pickuplimit.limits.api;

import net.minecraft.item.ItemStack;

public interface ILimitGroup {
    String getName();

    String getLimitMessage();

    int getLimit();

    int getStackLimitValue(ItemStack stack);
}
