package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LibZenClasses.PickupLimit)
public class PickupLimitCT {
    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, IIngredient... items) {
        new PickupLimitBuilderCT(groupName, defaultLimit)
                .addItems(items)
                .build();
    }

    @ZenMethod
    public static void simplePickupLimit(String groupName, int defaultLimit, String message, IIngredient... items) {
        new PickupLimitBuilderCT(groupName, defaultLimit)
                .addItems(items)
                .setLimitMessage(message)
                .build();
    }

    @ZenMethod
    public static PickupLimitBuilderCT pickupLimitBuilder(String groupName, int defaultLimit) {
        return new PickupLimitBuilderCT(groupName, defaultLimit);
    }
}
