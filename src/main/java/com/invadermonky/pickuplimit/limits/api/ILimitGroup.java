package com.invadermonky.pickuplimit.limits.api;

import net.minecraft.item.ItemStack;

public interface ILimitGroup {
    /**
     * Returns the name of the limit group.
     */
    String getName();

    /**
     * Returns the untranslated limit group limit message
     */
    String getLimitMessage();

    /**
     * Temporarily modifies the message
     */
    void setLimitMessage(String limitMessage);

    /**
     * Returns the current running inventory limit total.
     */
    int getInvCount();

    /**
     * Sets the current running inventory limit total.
     */
    void setInvCount(int invCount);

    /**
     * Returns the limit of the current group. This includes any adjustments made for player equipment or gamestage.
     */
    int getLimit();

    /**
     * Uses the passed ItemStack to determine the default item limit value.
     */
    int getDefaultLimitValue(ItemStack stack);
}
