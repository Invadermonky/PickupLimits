package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LibZenClasses.PickupLimitBuilder)
public class PickupLimitBuilderCT {
    private PickupLimitBuilder builder;

    public PickupLimitBuilderCT(String groupName, int defaultLimit) {
        this.builder = new PickupLimitBuilder(groupName, defaultLimit);
    }

    @ZenMethod
    public PickupLimitBuilderCT addStacks(IItemStack... stacks) {
        this.builder.addStacksToGroup(CraftTweakerMC.getItemStacks(stacks));
        return this;
    }

    @ZenMethod
    public PickupLimitBuilderCT addOreDict(IOreDictEntry oreDictEntry) {
        this.builder.addOreDictToGroup(oreDictEntry.getName());
        return this;
    }

    @ZenMethod
    public PickupLimitBuilderCT setLimitMessage(String message) {
        this.builder.setLimitMessage(message);
        return this;
    }

    @ZenMethod
    public PickupLimitBuilderCT addArmorLimitAdjustment(IItemStack armorStack, int adjustment) {
        this.builder.addArmorLimitAdjustment(CraftTweakerMC.getItemStack(armorStack), adjustment);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.baubles)
    public PickupLimitBuilderCT addBaubleLimitAdjustment(IItemStack baubleStack, int adjustment) {
        this.builder.addBaubleLimitAdjustment(CraftTweakerMC.getItemStack(baubleStack), adjustment);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilderCT addStageLimitOverride(String stageName, int limitOverride) {
        this.builder.addStageLimitOverride(stageName, limitOverride);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilderCT addStagedStackRemovals(String stageName, IItemStack... stacks) {
        this.builder.addStagedStackGroupRemoval(stageName, CraftTweakerMC.getItemStacks(stacks));
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitBuilderCT addStagedOreRemovals(String stageName, IOreDictEntry... oreDictEntries) {
        for(IOreDictEntry oreDictEntry : oreDictEntries) {
            this.builder.addStagedOreGroupRemoval(stageName, oreDictEntry.getName());
        }
        return this;
    }

    @ZenMethod
    public void build() {
        LimitRegistry.addPickupLimitGroup(this.builder.build());
    }
}
