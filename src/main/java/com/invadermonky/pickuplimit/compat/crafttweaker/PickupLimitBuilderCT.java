package com.invadermonky.pickuplimit.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.pickuplimit.PickupLimit")
public class PickupLimitBuilderCT {
    @ZenMethod
    public static void newSimpleLimit(String groupName, int defaultLimit, IItemStack... groupStacks) {
        new PickupLimitPrimerCT(groupName, defaultLimit)
                .addStacksToGroup(groupStacks)
                .build();
    }

    @ZenMethod
    public static void newSimpleLimit(String groupName, int defaultLimit, IOreDictEntry... iOreDictEntries) {
        PickupLimitPrimerCT primer = new PickupLimitPrimerCT(groupName, defaultLimit);
        for(IOreDictEntry oreDictEntry : iOreDictEntries) {
            primer.addOreDictToGroup(oreDictEntry);
        }
        primer.build();
    }

    @ZenMethod
    public static PickupLimitPrimerCT newLimit(String groupName, int defaultLimit) {
        return new PickupLimitPrimerCT(groupName, defaultLimit);
    }
}
