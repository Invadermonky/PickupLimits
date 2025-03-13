package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LibZenClasses.PickupLimit)
public class PickupLimitCT {
    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, IItemStack... groupStacks) {
        new PickupLimitBuilderCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .build();
    }

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, String message, IItemStack... groupStacks) {
        new PickupLimitBuilderCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .setLimitMessage(message)
                .build();
    }

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, IOreDictEntry... iOreDictEntries) {
        PickupLimitBuilderCT primer = new PickupLimitBuilderCT(groupName, defaultLimit);
        for(IOreDictEntry oreDictEntry : iOreDictEntries) {
            primer.addOreDict(oreDictEntry);
        }
        primer.build();
    }

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, String message, IOreDictEntry... iOreDictEntries) {
        PickupLimitBuilderCT primer = new PickupLimitBuilderCT(groupName, defaultLimit);
        for(IOreDictEntry oreDictEntry : iOreDictEntries) {
            primer.addOreDict(oreDictEntry);
        }
        primer.setLimitMessage(message);
        primer.build();
    }

    @ZenMethod
    public static PickupLimitBuilderCT pickupLimitBuilder(String groupName, int defaultLimit) {
        return new PickupLimitBuilderCT(groupName, defaultLimit);
    }
}
