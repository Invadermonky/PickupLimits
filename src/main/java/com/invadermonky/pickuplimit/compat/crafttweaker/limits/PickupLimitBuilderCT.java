package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.compat.crafttweaker.api.ILimitBuilderCT;
import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass(LibZenClasses.PickupLimitBuilder)
@ZenRegister
public class PickupLimitBuilderCT extends ILimitBuilderCT<PickupLimitBuilderCT, PickupLimitBuilder> {
    public PickupLimitBuilderCT(String groupName, int defaultLimit) {
        super(new PickupLimitBuilder(groupName, defaultLimit));
    }

    @Override
    public PickupLimitBuilderCT getThis() {
        return this;
    }

    @Override
    public void register() {
        CraftTweakerAPI.logInfo(String.format("Registering Pickup Limit group '%s'", this.getBuilder().getGroupName()));
        if (LimitRegistry.getAllEquipmentLimitGroups().containsKey(this.getBuilder().getGroupName())) {
            CraftTweakerAPI.logWarning("Duplicate group name found. Previous limit group will be overwritten.");
        }
        if (this.getBuilder().getGroupItems().isEmpty()) {
            CraftTweakerAPI.logWarning("Pickup limit group is empty. No items or valid oreDicts are registered.");
        }
        LimitRegistry.addPickupLimitGroup(this.getBuilder().build());
    }
}
