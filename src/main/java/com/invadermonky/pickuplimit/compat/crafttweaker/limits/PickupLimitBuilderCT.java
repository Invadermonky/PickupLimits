package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.PickupLimits;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(PickupLimitBuilderCT.CLASS)
public class PickupLimitBuilderCT {
    public static final String CLASS = "mods." + PickupLimits.MOD_ID + ".PickupLimit";

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, IItemStack... groupStacks) {
        new PickupLimitPrimerCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .build();
    }

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, String message, IItemStack... groupStacks) {
        new PickupLimitPrimerCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .setLimitMessage(message)
                .build();
    }

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, IOreDictEntry... iOreDictEntries) {
        PickupLimitPrimerCT primer = new PickupLimitPrimerCT(groupName, defaultLimit);
        for(IOreDictEntry oreDictEntry : iOreDictEntries) {
            primer.addOreDict(oreDictEntry);
        }
        primer.build();
    }

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, String message, IOreDictEntry... iOreDictEntries) {
        PickupLimitPrimerCT primer = new PickupLimitPrimerCT(groupName, defaultLimit);
        for(IOreDictEntry oreDictEntry : iOreDictEntries) {
            primer.addOreDict(oreDictEntry);
        }
        primer.setLimitMessage(message);
        primer.build();
    }

    @ZenMethod
    public static PickupLimitPrimerCT pickupLimitBuilder(String groupName, int defaultLimit) {
        return new PickupLimitPrimerCT(groupName, defaultLimit);
    }
}
