package com.invadermonky.pickuplimit.compat.crafttweaker.api;

import com.invadermonky.pickuplimit.limits.builders.AbstractLimitBuilder;
import com.invadermonky.pickuplimit.util.libs.LibZenClasses;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.enchantments.IEnchantmentDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotion;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

//I know it's not an interface, but labeling it as ILimitBuilder is a good indication that it doesn't do anything on its own.
@ZenClass(LibZenClasses.ILimitBuilder)
@ZenRegister
public abstract class ILimitBuilderCT<T extends ILimitBuilderCT<T, S>, S extends AbstractLimitBuilder<?, ?>> {
    private final S builder;

    public ILimitBuilderCT(S builder) {
        this.builder = builder;
    }

    public abstract T getThis();

    public S getBuilder() {
        return this.builder;
    }

    @ZenMethod
    public T addStacks(IItemStack... stacks) {
        this.getBuilder().addStacksToGroup(CraftTweakerMC.getItemStacks(stacks));
        return this.getThis();
    }

    @ZenMethod
    public T setStackLimitFunction(ILimitFunctionCT function) {
        this.getBuilder().setItemLimitValueFunction((player, stack, group) -> function.process(
                CraftTweakerMC.getIPlayer(player),
                CraftTweakerMC.getIItemStack(stack),
                new ILimitGroupCT() {
                    @Override
                    public String getName() {
                        return group.getName();
                    }

                    @Override
                    public String getLimitMessage() {
                        return group.getLimitMessage();
                    }

                    @Override
                    public void setLimitMessage(String message) {
                        group.setLimitMessage(message);
                    }

                    @Override
                    public int getInvCount() {
                        return group.getInvCount();
                    }

                    @Override
                    public void setInvCount(int invCount) {
                        group.setInvCount(invCount);
                    }

                    @Override
                    public int getLimit() {
                        return group.getLimit();
                    }

                    @Override
                    public int getDefaultLimitValue(IItemStack stack) {
                        return group.getDefaultLimitValue(CraftTweakerMC.getItemStack(stack));
                    }
                }
        ));
        return this.getThis();
    }

    @ZenMethod
    public T setAllowOverLimit() {
        this.getBuilder().setAllowOverlimit();
        return this.getThis();
    }

    @ZenMethod
    public T addEncumberedEffect(IPotion potion, int amplifier) {
        this.getBuilder().addEncumberedEffect(CraftTweakerMC.getPotion(potion), amplifier);
        return this.getThis();
    }

    @ZenMethod
    public T setLimitMessage(String message) {
        this.getBuilder().setLimitMessage(message);
        return this.getThis();
    }

    @ZenMethod
    public T setLimitTooltip(String tooltip) {
        this.getBuilder().setLimitTooltip(tooltip);
        return this.getThis();
    }

    @ZenMethod
    public T addArmorLimitAdjustment(IItemStack armorStack, int adjustment) {
        this.getBuilder().addArmorLimitAdjustment(CraftTweakerMC.getItemStack(armorStack), adjustment);
        return this.getThis();
    }

    @Optional.Method(modid = ModIds.ConstIds.baubles)
    @ZenMethod
    public T addBaubleLimitAdjustment(IItemStack baubleStack, int adjustment) {
        this.getBuilder().addBaubleLimitAdjustment(CraftTweakerMC.getItemStack(baubleStack), adjustment);
        return this.getThis();
    }

    @ZenMethod
    public T addEnchantmentLimitAdjustment(IEnchantmentDefinition enchantment, int enchantmentLevel, int adjustment) {
        Enchantment enchant = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantment.getRegistryName()));
        this.getBuilder().addEnchantmentLimitAdjustment(enchant, enchantmentLevel, adjustment);
        return this.getThis();
    }

    @ZenMethod
    public T addPotionLimitAdjustment(IPotion potion, int amplifier, int adjustment) {
        this.getBuilder().addPotionLimitAdjustment(CraftTweakerMC.getPotion(potion), amplifier, adjustment);
        return this.getThis();
    }

    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    @ZenMethod
    public T addStagedLimitOverride(String stageName, int limitOverride) {
        this.getBuilder().addStagedLimitOverride(stageName, limitOverride);
        return this.getThis();
    }

    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    @ZenMethod
    public T addStagedStackRemovals(String stageName, IItemStack... stacks) {
        this.getBuilder().addStagedStackGroupRemoval(stageName, CraftTweakerMC.getItemStacks(stacks));
        return this.getThis();
    }

    @ZenMethod
    public void build() {
        this.register();
    }

    @ZenMethod
    public abstract void register();
}
