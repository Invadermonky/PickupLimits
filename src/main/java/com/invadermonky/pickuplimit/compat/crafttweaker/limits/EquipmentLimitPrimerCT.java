package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.builders.EquipmentLimitBuilder;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantmentDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(EquipmentLimitPrimerCT.CLASS)
public class EquipmentLimitPrimerCT {
    public static final String CLASS = "mods." + PickupLimits.MOD_ID + ".EquipmentLimitBuilder";

    private EquipmentLimitBuilder builder;

    public EquipmentLimitPrimerCT(String groupName, int defaultLimit) {
        this.builder = new EquipmentLimitBuilder(groupName, defaultLimit);
    }

    @ZenMethod
    public EquipmentLimitPrimerCT addStacks(IItemStack... stacks) {
        this.builder.addStacksToGroup(CraftTweakerMC.getItemStacks(stacks));
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT addEnchantments(IEnchantmentDefinition... iEnchantments) {
        Enchantment[] enchants = new Enchantment[iEnchantments.length];
        for(int i = 0; i < enchants.length; i++) {
            enchants[i] = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(iEnchantments[i].getRegistryName()));
        }
        this.builder.addEnchantsToGroup(enchants);
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT setMatchAnyEnchant() {
        this.builder.setMatchAnyEnchant();
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT setIgnoreItemEnchantmentCount() {
        this.builder.setIgnoreItemEnchantmentCount();
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT setIgnoreEnchantmentLevel() {
        this.builder.setIgnoreEnchantmentLevel();
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT setCheckMainhand() {
        this.builder.setCheckMainhand();
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT setCheckOffhand() {
        this.builder.setCheckOffhand();
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT setLimitMessage(String message) {
        this.builder.setLimitMessage(message);
        return this;
    }

    @ZenMethod
    public EquipmentLimitPrimerCT addArmorLimitAdjustment(IItemStack armorStack, int adjustment) {
        this.builder.addArmorLimitAdjustment(CraftTweakerMC.getItemStack(armorStack), adjustment);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.baubles)
    public EquipmentLimitPrimerCT addBaubleLimitAdjustment(IItemStack baubleStack, int adjustment) {
        this.builder.addBaubleLimitAdjustment(CraftTweakerMC.getItemStack(baubleStack), adjustment);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public EquipmentLimitPrimerCT addStageLimitOverride(String stageName, int limitOverride) {
        this.builder.addStageLimitOverride(stageName, limitOverride);
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public EquipmentLimitPrimerCT addStagedStackRemovals(String stageName, IItemStack... stacks) {
        this.builder.addStagedStackGroupRemoval(stageName, CraftTweakerMC.getItemStacks(stacks));
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public EquipmentLimitPrimerCT addStagedEnchantmentRemovals(String stageName, IEnchantmentDefinition... iEnchantments) {
        Enchantment[] enchants = new Enchantment[iEnchantments.length];
        for(int i = 0; i < enchants.length; i++) {
            enchants[i] = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(iEnchantments[i].getRegistryName()));
        }
        this.builder.addStagedEnchantRemoval(stageName, enchants);
        return this;
    }

    @ZenMethod
    public void build() {
        LimitRegistry.addEquipmentLimitGroup(this.builder.build());
    }
}
