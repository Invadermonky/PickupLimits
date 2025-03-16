package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.compat.crafttweaker.api.ILimitBuilderCT;
import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

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

    @ZenMethod
    public PickupLimitBuilderCT addOreDict(IOreDictEntry oreDictEntry) {
        this.getBuilder().addOreDictToGroup(oreDictEntry.getName());
        return this;
    }

    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    @ZenMethod
    public PickupLimitBuilderCT addStagedOreRemovals(String stageName, IOreDictEntry... oreDictEntries) {
        for(IOreDictEntry oreDictEntry : oreDictEntries) {
            this.getBuilder().addStagedOreGroupRemoval(stageName, oreDictEntry.getName());
        }
        return this;
    }

    @Override
    public void register() {
        CraftTweakerAPI.logInfo(String.format("Registering Pickup Limit group '%s'", this.getBuilder().getGroupName()));
        if(LimitRegistry.getAllEquipmentLimitGroups().containsKey(this.getBuilder().getGroupName())) {
            CraftTweakerAPI.logWarning("Duplicate group name found. Previous limit group will be overwritten.");
        }
        if(this.getBuilder().getGroupStacks().isEmpty()) {
            CraftTweakerAPI.logWarning("Pickup limit group is empty. No items or valid oreDicts are registered.");
        }
        LimitRegistry.addPickupLimitGroup(this.getBuilder().build());
    }
}
