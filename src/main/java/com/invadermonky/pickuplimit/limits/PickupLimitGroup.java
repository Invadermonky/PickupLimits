package com.invadermonky.pickuplimit.limits;

import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.limits.util.AbstractLimitGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PickupLimitGroup extends AbstractLimitGroup<PickupLimitBuilder> {
    public static final PickupLimitGroup EMPTY = new PickupLimitBuilder(null, -1).build();

    public PickupLimitGroup(PickupLimitBuilder builder) {
        super(builder);
    }

    @Override
    public int getStackLimitValue(EntityPlayer player, ItemStack stack) {
        return stack.getCount();
    }
}
