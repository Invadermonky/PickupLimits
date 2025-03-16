package com.invadermonky.pickuplimit.limits.builders;

import com.invadermonky.pickuplimit.limits.groups.PickupLimitGroup;
import com.invadermonky.pickuplimit.util.StringHelper;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;

public class PickupLimitBuilder extends AbstractLimitBuilder<PickupLimitBuilder, PickupLimitGroup> {

    public PickupLimitBuilder(String groupName, int defaultLimit) {
        super(groupName, defaultLimit);
        this.setLimitMessage(StringHelper.getTranslationKey("no_pickup", "chat"));
    }

    @Override
    protected PickupLimitBuilder getThis() {
        return this;
    }

    /**
     * Any ItemStacks associated with the passed ore dictionary string will be removed from this group once the player
     * obtains the specified GameStage.
     *
     * @param stageName The name of the GameStage
     * @param oreDict The ore dictionary string
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilder addStagedOreGroupRemoval(String stageName, String oreDict) {
        NonNullList<ItemStack> stackList = NonNullList.create();
        stackList.addAll(OreDictionary.getOres(oreDict));
        return this.addStagedStackGroupRemoval(stageName, stackList.toArray(new ItemStack[0]));
    }

    /**
     * Adds all ItemStacks associated with the passed ore dictionary string to the group ItemStack list.
     *
     * @param oreDict The ore dictionary string
     * @return this
     */
    public PickupLimitBuilder addOreDictToGroup(String oreDict) {
        this.groupStacks.addAll(OreDictionary.getOres(oreDict));
        return this;
    }

    @Override
    public PickupLimitGroup build() {
        return new PickupLimitGroup(this);
    }
}
