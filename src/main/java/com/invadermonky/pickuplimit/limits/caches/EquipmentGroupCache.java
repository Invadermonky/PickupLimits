package com.invadermonky.pickuplimit.limits.caches;

import com.invadermonky.pickuplimit.limits.groups.EquipmentLimitGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EquipmentGroupCache extends AbstractGroupCache<EquipmentLimitGroup> {
    public EquipmentGroupCache(EntityPlayer player, EquipmentLimitGroup group, ItemStack stack) {
        super(player, group, stack);
    }

    public EquipmentGroupCache(EntityPlayer player, EquipmentLimitGroup group) {
        super(player, group);
    }

    @Override
    public boolean handleLimitDrop(ItemStack stack, boolean dropItem) {
        return this.group.handleLimitDrop(this.player, stack, this, dropItem && this.shouldItemBeDropped());
    }
}
