package com.invadermonky.pickuplimit.compat.groovy.limits;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderRegistrationMethod;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.limits.api.ILimitFunction;
import com.invadermonky.pickuplimit.limits.builders.EquipmentLimitBuilder;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

public class EquipmentLimit extends VirtualizedRegistry<EquipmentLimitGroup> {
    @Override
    @GroovyBlacklist
    public void onReload() {
        LimitRegistry.removeAllEquipmentLimitGroups();
    }

    @GroovyBlacklist
    public RecipeBuilder recipeBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void simpleEquipmentLimit(String groupName, int defaultLimit, ItemStack... stacks) {
        this.simpleEquipmentLimit(groupName, defaultLimit, null, stacks);
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void simpleEquipmentLimit(String groupName, int defaultLimit, @Nullable String message, ItemStack... stacks) {
        this.recipeBuilder(groupName, defaultLimit)
                .setLimitMessage(message)
                .addStacks(stacks)
                .register();
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void simpleEnchantmentLimit(String groupName, int defaultLimit, Enchantment... enchants) {
        this.simpleEnchantmentLimit(groupName, defaultLimit, null, enchants);
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void simpleEnchantmentLimit(String groupName, int defaultLimit, @Nullable String message, Enchantment... enchants) {
        this.recipeBuilder(groupName,defaultLimit)
                .setLimitMessage(message)
                .addEnchantments(enchants)
                .setCheckOffhand()
                .register();
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void equippedEnchantedItemLimit(int defaultLimit, boolean checkMainhand) {
        equippedEnchantedItemLimit(defaultLimit, null, checkMainhand);
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void equippedEnchantedItemLimit(int defaultLimit, @Nullable String message, boolean checkMainhand) {
        RecipeBuilder builder = this.recipeBuilder(PickupLimits.MOD_ID + ":equipped_enchant_limit", defaultLimit)
                .setLimitMessage(message)
                .setMatchAnyEnchant()
                .setIgnoreItemEnchantmentCount()
                .setCheckOffhand();
        if(checkMainhand)
            builder.setCheckMainhand();

        builder.register();
    }

    @RecipeBuilderDescription
    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public RecipeBuilder equipmentLimitBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }



    public static class RecipeBuilder extends AbstractRecipeBuilder<EquipmentLimitGroup> {
        private final EquipmentLimitBuilder builder;

        public RecipeBuilder(String groupName, int defaultLimit) {
            this.builder = new EquipmentLimitBuilder(groupName, defaultLimit);
        }

        @RecipeBuilderDescription
        public RecipeBuilder addStacks(ItemStack... stacks) {
            this.builder.addStacksToGroup(stacks);
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder addEnchantments(Enchantment... enchants) {
            this.builder.addEnchantsToGroup(enchants);
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder setMatchAnyEnchant() {
            this.builder.setMatchAnyEnchant();
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder setIgnoreItemEnchantmentCount() {
            this.builder.setIgnoreItemEnchantmentCount();
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder setIgnoreEnchantmentLevel() {
            this.builder.setIgnoreEnchantmentLevel();
            return this;
        }

        @RecipeBuilderDescription(
                example = {
                        @Example("(player, stack, group) -> { return stack.getCount() }")
                }
        )
        public RecipeBuilder setStackLimitFunction(ILimitFunction function) {
            this.builder.setItemLimitValueFunction(function);
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder setCheckMainhand() {
            this.builder.setCheckMainhand();
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder setCheckOffhand() {
            this.builder.setCheckOffhand();
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder setLimitMessage(@Nullable String pickupLimitMessage) {
            this.builder.setLimitMessage(pickupLimitMessage);
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder addArmorLimitAdjustment(ItemStack stack, int adjustment) {
            this.builder.addArmorLimitAdjustment(stack, adjustment);
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.baubles)
        @RecipeBuilderDescription
        public RecipeBuilder addBaubleLimitAdjustment(ItemStack stack, int adjustment) {
            this.builder.addBaubleLimitAdjustment(stack, adjustment);
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.gamestages)
        @RecipeBuilderDescription
        public RecipeBuilder addStageLimitOverride(String stageName, int limitOverride) {
            this.builder.addStageLimitOverride(stageName, limitOverride);
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.gamestages)
        @RecipeBuilderDescription
        public RecipeBuilder addStagedStackRemovals(String stageName, ItemStack... stacks) {
            this.builder.addStagedStackGroupRemoval(stageName, stacks);
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.gamestages)
        @RecipeBuilderDescription
        public RecipeBuilder addStagedEnchantRemovals(String stageName, Enchantment... enchants) {
            this.builder.addStagedEnchantRemoval(stageName, enchants);
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Error creating new Pickup Limit group.";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {

        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable EquipmentLimitGroup register() {
            if(!validate()) return null;
            EquipmentLimitGroup group = this.builder.build();
            LimitRegistry.addEquipmentLimitGroup(group);
            return group;
        }
    }
}
