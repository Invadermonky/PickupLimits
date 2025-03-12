package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantment;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(EquipmentLimitBuilderCT.CLASS)
public class EquipmentLimitBuilderCT {
    public static final String CLASS = "mods.pickuplimit.EquipmentLimit";

    @ZenMethod
    public static void simpleEquipmentLimit(String groupName, int defaultLimit, IItemStack... groupStacks) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .build();
    }

    @ZenMethod
    public static void simpleEquipmentLimit(String groupName, int defaultLimit, String message, IItemStack... groupStacks) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .addStacks(groupStacks)
                .setLimitMessage(message)
                .build();
    }

    @ZenMethod
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit, IEnchantment... enchantments) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .addEnchantments(enchantments)
                .build();
    }

    @ZenMethod
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit, String message, IEnchantment... enchantments) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .setLimitMessage(message)
                .addEnchantments(enchantments)
                .build();
    }

    @ZenMethod
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .setMatchAnyEnchant()
                .build();
    }

    @ZenMethod
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit, String message) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .setLimitMessage(message)
                .setMatchAnyEnchant()
                .build();
    }

    @ZenMethod
    public static EquipmentLimitPrimerCT newEquipmentLimit(String groupName, int defaultLimit) {
        return new EquipmentLimitPrimerCT(groupName, defaultLimit);
    }
}
