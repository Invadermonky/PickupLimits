package com.invadermonky.pickuplimit.limits;

import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.limits.util.AbstractLimitGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class PickupLimitGroup extends AbstractLimitGroup<PickupLimitBuilder> {
    public static final PickupLimitGroup EMPTY = new PickupLimitBuilder(null, -1).build();

    public PickupLimitGroup(PickupLimitBuilder builder) {
        super(builder);
    }

    @Override
    public int getStackLimitValue(EntityPlayer player, ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public boolean matches(EntityPlayer player, ItemStack stack) {
        //Values of -1 means the limit is not active.
        if(!stack.isEmpty() && this.getLimitWithStageOverride(player) != -1) {
            for (ItemStack limitStack : this.getLimitStacks(player)) {
                if (OreDictionary.itemMatches(limitStack, stack, false)) {
                    if (!limitStack.hasTagCompound() || ItemStack.areItemStackTagsEqual(stack, limitStack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
