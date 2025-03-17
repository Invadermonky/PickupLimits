package com.invadermonky.pickuplimit.compat.groovy.limits;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.api.ILimitFunction;
import com.invadermonky.pickuplimit.limits.builders.EquipmentLimitBuilder;
import com.invadermonky.pickuplimit.limits.groups.EquipmentLimitGroup;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

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
                    @Example(value = "'protection', 8, 'your.translation.key', enchantment('minecraft:protection')"),
                    @Example(value = "'protection', 8, 'You can only equip up to 8 levels of Protection', enchantment('minecraft:protection')")
            },
            priority = 1001
    )
    public void simpleEnchantmentLimit(String groupName, int defaultLimit, @Nullable String message, Enchantment... enchants) {
        this.recipeBuilder(groupName, defaultLimit)
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
        if (checkMainhand)
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
        @Property(comp = @Comp(gte = 0), priority = 1000, value = "groovyscript.wiki.pickuplimits.limit_builder.addStacks.value")
        private List<ItemStack> addStacks;
        @Property(comp = @Comp(gte = 0), priority = 1001)
        private List<Enchantment> addEnchantments;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1002)
        private boolean setMatchAnyEnchant;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1003)
        private boolean setIgnoreItemEnchantmentCount;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1004)
        private boolean setIgnoreEnchantmentLevel;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1005)
        private boolean setCheckMainhand;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1006)
        private boolean setCheckOffhand;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1007, value = "groovyscript.wiki.pickuplimits.limit_builder.setStackLimitFunction.value")
        private ILimitFunction setStackLimitFunction;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1008, value = "groovyscript.wiki.pickuplimits.limit_builder.setAllowOverLimit.value")
        private boolean setAllowOverLimit;
        @Property(comp = @Comp(gte = 0), priority = 1008, value = "groovyscript.wiki.pickuplimits.limit_builder.addEncumberedEffect.value")
        private List<Potion> addEncumberedEffect;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1009, value = "groovyscript.wiki.pickuplimits.limit_builder.setLimitMessage.value")
        private String setLimitMessage;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1010,  value = "groovyscript.wiki.pickuplimits.limit_builder.setLimitTooltip.value")
        private String setLimitTooltip;
        @Property(comp = @Comp(gte = 0), priority = 10011, value = "groovyscript.wiki.pickuplimits.limit_builder.addArmorLimitAdjustment.value")
        private Map<ItemStack, Integer> addArmorLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1012, value = "groovyscript.wiki.pickuplimits.limit_builder.addBaubleLimitAdjustment.value")
        private Map<ItemStack, Integer> addBaubleLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1013, value = "groovyscript.wiki.pickuplimits.limit_builder.addEnchantmentLimitAdjustment.value")
        private Map<Enchantment, Integer> addEnchantmentLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1014, value = "groovyscript.wiki.pickuplimits.limit_builder.addPotionLimitAdjustment.value")
        private Map<Potion, Integer> addPotionLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1015, value = "groovyscript.wiki.pickuplimits.limit_builder.addStagedLimitOverride.value")
        private Map<String, Integer> addStagedLimitOverride;
        @Property(comp = @Comp(gte = 0), priority = 1016, value = "groovyscript.wiki.pickuplimits.limit_builder.addStagedStackRemovals.value")
        private Map<String, List<ItemStack>> addStagedStackRemovals;
        @Property(comp = @Comp(gte = 0), priority = 1017)
        private Map<String, List<OreDictIngredient>> addStagedEnchantmentRemovals;

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
            if (!validate()) return null;
            EquipmentLimitGroup group = this.getBuilder().build();
            LimitRegistry.addEquipmentLimitGroup(group);
            return group;
        }
    }
}
