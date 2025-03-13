package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.PickupLimits;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantmentDefinition;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(EquipmentLimitBuilderCT.CLASS)
public class EquipmentLimitBuilderCT {
    public static final String CLASS = "mods." + PickupLimits.MOD_ID +".EquipmentLimit";

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
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit, IEnchantmentDefinition... enchantments) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .addEnchantments(enchantments)
                .setCheckOffhand()
                .build();
    }

    @ZenMethod
    public static void simpleEnchantmentLimit(String groupName, int defaultLimit, String message, IEnchantmentDefinition ... enchantments) {
        new EquipmentLimitPrimerCT(groupName, defaultLimit)
                .setLimitMessage(message)
                .addEnchantments(enchantments)
                .setCheckOffhand()
                .build();
    }

    @ZenMethod
    public static void equippedEnchantedItemLimit(int defaultLimit, boolean checkMainhand) {
        EquipmentLimitPrimerCT primer = new EquipmentLimitPrimerCT(PickupLimits.MOD_ID + ":equipped_enchant_limit", defaultLimit)
                .setMatchAnyEnchant()
                .setIgnoreItemEnchantmentCount()
                .setCheckOffhand();
        if(checkMainhand)
            primer.setCheckMainhand();
        primer.build();
    }

    @ZenMethod
    public static void equippedEnchantedItemLimit(int defaultLimit, String message, boolean checkMainhand) {
        EquipmentLimitPrimerCT primer = new EquipmentLimitPrimerCT(PickupLimits.MOD_ID + ":equipped_enchant_limit", defaultLimit)
                .setLimitMessage(message)
                .setMatchAnyEnchant()
                .setIgnoreItemEnchantmentCount()
                .setCheckOffhand();
        if(checkMainhand)
            primer.setCheckMainhand();
        primer.build();
    }

    @ZenMethod
    public static EquipmentLimitPrimerCT equipmentLimitBuilder(String groupName, int defaultLimit) {
        return new EquipmentLimitPrimerCT(groupName, defaultLimit);
    }
}
