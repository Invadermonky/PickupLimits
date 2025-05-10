package com.invadermonky.pickuplimit.limits.builders;

import com.invadermonky.pickuplimit.limits.groups.PickupLimitGroup;
import com.invadermonky.pickuplimit.util.StringHelper;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.Optional;

public class PickupLimitBuilder extends AbstractLimitBuilder<PickupLimitBuilder, PickupLimitGroup> {

    public PickupLimitBuilder(String groupName, int defaultLimit) {
        super(groupName, defaultLimit);
        this.setLimitMessage(StringHelper.getTranslationKey("no_pickup", "chat"));
    }

    @Override
    protected PickupLimitBuilder getThis() {
        return this;
    }

    @Override
    public PickupLimitGroup build() {
        return new PickupLimitGroup(this);
    }

    /**
     * Any ItemStacks associated with the passed ore dictionary string will be removed from this group once the player
     * obtains the specified GameStage.
     *
     * @param stageName  The name of the GameStage
     * @param ingredient The ingredient value
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilder addStagedGroupRemoval(String stageName, Ingredient ingredient) {
        return this.addStagedIngredientRemoval(stageName, ingredient);
    }
}
