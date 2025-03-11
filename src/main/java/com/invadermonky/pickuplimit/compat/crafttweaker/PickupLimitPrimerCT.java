package com.invadermonky.pickuplimit.compat.crafttweaker;

import com.invadermonky.pickuplimit.limits.PickupLimitBuilder;
import com.invadermonky.pickuplimit.limits.PickupLimitRegistry;
import com.invadermonky.pickuplimit.util.ModIds;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(PickupLimitPrimerCT.CLASS)
public class PickupLimitPrimerCT {
    public static final String NAME = "PickupLimit";
    public static final String CLASS = "mods.pickuplimit.PickupLimitBuilder";

    private PickupLimitBuilder builder;

    public PickupLimitPrimerCT(String groupName, int defaultLimit) {
        this.builder = new PickupLimitBuilder(groupName, defaultLimit);
    }

    @ZenMethod
    public PickupLimitPrimerCT addStacksToGroup(IItemStack... stacks) {
        this.builder.addStacksToGroup(CraftTweakerMC.getItemStacks(stacks));
        return this;
    }

    @ZenMethod
    public PickupLimitPrimerCT addOreDictToGroup(IOreDictEntry oreDictEntry) {
        this.builder.addOreDictToGroup(oreDictEntry.getName());
        return this;
    }

    @ZenMethod
    public PickupLimitPrimerCT setPickupLimitMessage(String message) {
        this.builder.setPickupLimitMessage(message);
        return this;
    }

    @ZenMethod
    public PickupLimitPrimerCT addArmorLimitAdjustment(IItemStack armorStack, int adjustment) {
        this.builder.addArmorLimitAdjustment(CraftTweakerMC.getItemStack(armorStack), adjustment);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.baubles)
    public PickupLimitPrimerCT addBaubleLimitAdjustment(IItemStack baubleStack, int adjustment) {
        this.builder.addBaubleLimitAdjustment(CraftTweakerMC.getItemStack(baubleStack), adjustment);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitPrimerCT addStageLimitOverride(String stageName, int limitOverride) {
        this.builder.addStageLimitOverride(stageName, limitOverride);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public PickupLimitPrimerCT addStagedStackRemovals(String stageName, IItemStack... stacks) {
        this.builder.addStagedStackGroupRemoval(stageName, CraftTweakerMC.getItemStacks(stacks));
        return this;
    }

    @ZenMethod
    public void build() {
        PickupLimitRegistry.addPickupLimitGroup(this.builder.build());
    }
}
