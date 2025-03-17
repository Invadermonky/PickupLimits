package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantmentDefinition;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LibZenClasses.EquipmentLimit)
public class EquipmentLimitCT {
    @ZenMethod
    public static void simpleEquipmentLimit(String groupName, int defaultLimit, IItemStack... groupStacks) {
        new EquipmentLimitBuilderCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .build();
    }

    @ZenMethod
    public static void simpleEquipmentLimit(String groupName, int defaultLimit, String message, IItemStack... groupStacks) {
        new EquipmentLimitBuilderCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .setLimitMessage(message)
                .build();
    }

    @ZenMethod
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit, IEnchantmentDefinition... enchantments) {
        new EquipmentLimitBuilderCT(groupName, defaultLimit)
                .addEnchantments(enchantments)
                .setCheckOffhand()
                .build();
    }

    @ZenMethod
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit, String message, IEnchantmentDefinition... enchantments) {
        EquipmentLimitBuilderCT primer = new EquipmentLimitBuilderCT(groupName, defaultLimit)
                .addEnchantments(enchantments)
                .setCheckOffhand();
        if (message != null)
            primer.setLimitMessage(message);
        primer.build();
    }

    @ZenMethod
    public static void equippedEnchantedItemLimit(int defaultLimit, @Optional String message, @Optional boolean checkMainhand) {
        EquipmentLimitBuilderCT primer = new EquipmentLimitBuilderCT(PickupLimits.MOD_ID + ":equipped_enchant_limit", defaultLimit)
                .setMatchAnyEnchant()
                .setIgnoreItemEnchantmentCount()
                .setCheckOffhand();
        if (message != null)
            primer.setLimitMessage(message);
        if (checkMainhand)
            primer.setCheckMainhand();
        primer.build();
    }

    @ZenMethod
    public static EquipmentLimitBuilderCT equipmentLimitBuilder(String groupName, int defaultLimit) {
        return new EquipmentLimitBuilderCT(groupName, defaultLimit);
    }
}
