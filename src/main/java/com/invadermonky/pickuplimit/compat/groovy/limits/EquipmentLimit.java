package com.invadermonky.pickuplimit.compat.groovy.limits;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.builders.EquipmentLimitBuilder;
import com.invadermonky.pickuplimit.limits.groups.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = PickupLimits.MOD_ID)
public class EquipmentLimit extends VirtualizedRegistry<EquipmentLimitGroup> {
    //TODO: Finish method descriptions for GroovyScript wiki

    @Override
    @GroovyBlacklist
    public void onReload() {
        LimitRegistry.removeAllEquipmentLimitGroups();
    }

    @GroovyBlacklist
    public RecipeBuilder recipeBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'diamond_armor', 2, item('minecraft:diamond_helmet'), item('minecraft:diamond_chestplate'), item('minecraft:diamond_leggings'), item('minecraft:diamond_boots')")
            },
            priority = 1000
    )
    public void simpleEquipmentLimit(String groupName, int defaultLimit, ItemStack... stacks) {
        this.simpleEquipmentLimit(groupName, defaultLimit, null, stacks);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'diamond_armor', 2, 'your.translation.key', item('minecraft:diamond_helmet'), item('minecraft:diamond_chestplate'), item('minecraft:diamond_leggings'), item('minecraft:diamond_boots')"),
                    @Example("'diamond_armor', 2, 'You can only equip two pieces of diamond armor', item('minecraft:diamond_helmet'), item('minecraft:diamond_chestplate'), item('minecraft:diamond_leggings'), item('minecraft:diamond_boots')")
            },
            priority = 1000
    )
    public void simpleEquipmentLimit(String groupName, int defaultLimit, @Nullable String message, ItemStack... stacks) {
        this.recipeBuilder(groupName, defaultLimit)
                .setLimitMessage(message)
                .addStacks(stacks)
                .register();
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'protection', 8, enchantment('minecraft:protection')")
            },
            priority = 1001
    )
    public void simpleEnchantmentLimit(String groupName, int defaultLimit, Enchantment... enchants) {
        this.simpleEnchantmentLimit(groupName, defaultLimit, null, enchants);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'protection', 8, 'your.translation.key', enchantment('minecraft:protection')"),
                    @Example("'protection', 8, 'You can only equip up to level VIII Protection', enchantment('minecraft:protection')")
            },
            priority = 1001
    )
    public void simpleEnchantmentLimit(String groupName, int defaultLimit, @Nullable String message, Enchantment... enchants) {
        this.recipeBuilder(groupName,defaultLimit)
                .setLimitMessage(message)
                .addEnchantments(enchants)
                .setCheckOffhand()
                .register();
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("2, false")
            },
            priority = 1002
    )
    public void equippedEnchantedItemLimit(int defaultLimit, boolean checkMainhand) {
        equippedEnchantedItemLimit(defaultLimit, null, checkMainhand);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("2, 'your.translation.key', false"),
                    @Example("2, 'You can only equip two enchanted items', false")
            },
            priority = 1002
    )
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
    public RecipeBuilder equipmentLimitBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }

    //###########################################################################
    //                                  Builder
    //###########################################################################

    public static class RecipeBuilder extends AbstractLimitRecipeBuilder<RecipeBuilder, EquipmentLimitBuilder, EquipmentLimitGroup> {
        @GroovyBlacklist
        public RecipeBuilder(String groupName, int defaultLimit) {
            super(new EquipmentLimitBuilder(groupName, defaultLimit));
        }

        @GroovyBlacklist
        @Override
        public RecipeBuilder getThis() {
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder addEnchantments(Enchantment... enchants) {
            this.getBuilder().addEnchantsToGroup(enchants);
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder setMatchAnyEnchant() {
            this.getBuilder().setMatchAnyEnchant();
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder setIgnoreItemEnchantmentCount() {
            this.getBuilder().setIgnoreItemEnchantmentCount();
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder setIgnoreEnchantmentLevel() {
            this.getBuilder().setIgnoreEnchantmentLevel();
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder setCheckMainhand() {
            this.getBuilder().setCheckMainhand();
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder setCheckOffhand() {
            this.getBuilder().setCheckOffhand();
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.gamestages)
        @RecipeBuilderMethodDescription
        public RecipeBuilder addStagedEnchantmentRemovals(String stageName, Enchantment... enchants) {
            this.getBuilder().addStagedEnchantmentRemoval(stageName, enchants);
            return this;
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            msg.add(LimitRegistry.getAllEquipmentLimitGroups().containsKey(this.getBuilder().getGroupName()), "Duplicate group name found for pickup limit group " + this.getBuilder().getGroupName());
            msg.add(this.getBuilder().getGroupStacks().isEmpty() && this.getBuilder().getGroupEnchants().isEmpty(), "Pickup limit group is empty. No items or valid oreDicts are registered");
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable EquipmentLimitGroup register() {
            GroovyLog.get().info(String.format("Registering Equipment Limit group: '%s'", this.getBuilder().getGroupName()));
            if(!validate()) return null;
            EquipmentLimitGroup group = this.getBuilder().build();
            LimitRegistry.addEquipmentLimitGroup(group);
            return group;
        }
    }
}
