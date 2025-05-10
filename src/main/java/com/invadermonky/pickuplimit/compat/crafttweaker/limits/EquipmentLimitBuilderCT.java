package com.invadermonky.pickuplimit.compat.crafttweaker.limits;

import com.invadermonky.pickuplimit.compat.crafttweaker.api.ILimitBuilderCT;
import com.invadermonky.pickuplimit.limits.builders.EquipmentLimitBuilder;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantmentDefinition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(LibZenClasses.EquipmentLimitBuilder)
@ZenRegister
public class EquipmentLimitBuilderCT extends ILimitBuilderCT<EquipmentLimitBuilderCT, EquipmentLimitBuilder> {
    public EquipmentLimitBuilderCT(String groupName, int defaultLimit) {
        super(new EquipmentLimitBuilder(groupName, defaultLimit));
    }

    @Override
    public EquipmentLimitBuilderCT getThis() {
        return this;
    }

    @ZenMethod
    @Override
    public void register() {
        CraftTweakerAPI.logInfo(String.format("Registering Equipment Limit group '%s'", this.getBuilder().getGroupName()));
        if (LimitRegistry.getAllEquipmentLimitGroups().containsKey(this.getBuilder().getGroupName())) {
            CraftTweakerAPI.logWarning("Duplicate group name found. Previous limit group will be overwritten.");
        }
        if (this.getBuilder().getGroupItems().isEmpty() && this.getBuilder().getGroupEnchants().isEmpty()) {
            CraftTweakerAPI.logWarning("Equipment Limit group is empty. No items or enchantments are registered.");
        }
        LimitRegistry.addEquipmentLimitGroup(this.getBuilder().build());
    }

    @ZenMethod
    public EquipmentLimitBuilderCT addEnchantments(IEnchantmentDefinition... iEnchantments) {
        Enchantment[] enchants = new Enchantment[iEnchantments.length];
        for (int i = 0; i < enchants.length; i++) {
            enchants[i] = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(iEnchantments[i].getRegistryName()));
        }
        this.getBuilder().addEnchantsToGroup(enchants);
        return this;
    }

    @ZenMethod
    public EquipmentLimitBuilderCT setMatchAnyEnchant() {
        this.getBuilder().setMatchAnyEnchant();
        return this;
    }

    @ZenMethod
    public EquipmentLimitBuilderCT setIgnoreItemEnchantmentCount() {
        this.getBuilder().setIgnoreItemEnchantmentCount();
        return this;
    }

    @ZenMethod
    public EquipmentLimitBuilderCT setIgnoreEnchantmentLevel() {
        this.getBuilder().setIgnoreEnchantmentLevel();
        return this;
    }

    @ZenMethod
    public EquipmentLimitBuilderCT setCheckMainhand() {
        this.getBuilder().setCheckMainhand();
        return this;
    }

    @ZenMethod
    public EquipmentLimitBuilderCT setCheckOffhand() {
        this.getBuilder().setCheckOffhand();
        return this;
    }

    @ZenMethod
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public EquipmentLimitBuilderCT addStagedEnchantmentRemovals(String stageName, IEnchantmentDefinition... iEnchantments) {
        Enchantment[] enchants = new Enchantment[iEnchantments.length];
        for (int i = 0; i < enchants.length; i++) {
            enchants[i] = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(iEnchantments[i].getRegistryName()));
        }
        this.getBuilder().addStagedEnchantmentRemoval(stageName, enchants);
        return this;
    }
}
