package com.invadermonky.pickuplimit.handlers.util;

import com.invadermonky.pickuplimit.limits.EquipmentLimitGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EquipmentGroupCache extends AbstractGroupCache<EquipmentLimitGroup> {
    public EquipmentGroupCache(EntityPlayer player, EquipmentLimitGroup group, ItemStack stack) {
        super(player, group, stack);
    }

    public EquipmentGroupCache(EntityPlayer player, EquipmentLimitGroup group) {
        super(player, group);
    }
}
