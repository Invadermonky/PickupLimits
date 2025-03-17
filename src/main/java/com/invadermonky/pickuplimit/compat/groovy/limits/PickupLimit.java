package com.invadermonky.pickuplimit.compat.groovy.limits;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.api.ILimitFunction;
import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.limits.groups.PickupLimitGroup;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@RegistryDescription(linkGenerator = PickupLimits.MOD_ID)
public class PickupLimit extends VirtualizedRegistry<PickupLimitGroup> {
    //TODO: Finish method descriptions for GroovyScript wiki

    @Override
    @GroovyBlacklist
    public void onReload() {
        LimitRegistry.removeAllPickupLimitGroups();
    }

    @GroovyBlacklist
    public RecipeBuilder recipeBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'stone', 256, item('minecraft:stone'), item('minecraft:cobblestone')")
            },
            description = "groovyscript.wiki.pickuplimits.pickup_limit.simpleItemPickupLimit.description",
            priority = 1000
    )
    public void simplePickupLimit(String groupName, int defaultLimit, ItemStack... stacks) {
        this.simplePickupLimit(groupName, defaultLimit, null, stacks);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'stone', 256, 'your.translation.key', item('minecraft:stone'), item('minecraft:cobblestone')"),
                    @Example("'stone', 256, 'You cannot carry any more stone', item('minecraft:stone'), item('minecraft:cobblestone')")
            },
            description = "groovyscript.wiki.pickuplimits.pickup_limit.simpleItemPickupLimit.message.description",
            priority = 1000
    )
    public void simplePickupLimit(String groupName, int defaultLimit, @Nullable String message, ItemStack... stacks) {
        this.recipeBuilder(groupName, defaultLimit)
                .addStacks(stacks)
                .setLimitMessage(message)
                .register();
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {@Example("'gems', 32, ore('gemDiamond'), ore('gemEmerald')")},
            description = "groovyscript.wiki.pickuplimits.pickup_limit.simpleOrePickupLimit.description",
            priority = 1001
    )
    public void simplePickupLimit(String groupName, int defaultLimit, OreDictIngredient... oreDicts) {
        this.simplePickupLimit(groupName, defaultLimit, null, oreDicts);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'gems', 32, 'your.translation.key', ore('gemDiamond'), ore('gemEmerald')"),
                    @Example("'gems', 32, 'Gems are falling out of your pockets', ore('gemDiamond'), ore('gemEmerald')")
            },
            description = "groovyscript.wiki.pickuplimits.pickup_limit.simpleOrePickupLimit.message.description",
            priority = 1001
    )
    public void simplePickupLimit(String groupName, int defaultLimit, @Nullable String message, OreDictIngredient... oreDicts) {
        RecipeBuilder builder = new RecipeBuilder(groupName, defaultLimit);
        for (OreDictIngredient oreDict : oreDicts) {
            builder.addOreDict(oreDict);
        }
        builder.setLimitMessage(message);
        builder.register();
    }

    @RecipeBuilderDescription
    public RecipeBuilder pickupLimitBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }

    //###########################################################################
    //                                  Builder
    //###########################################################################

    public static class RecipeBuilder extends AbstractLimitRecipeBuilder<RecipeBuilder, PickupLimitBuilder, PickupLimitGroup> {
        @Property(comp = @Comp(eq = 1), priority = 999)
        private RecipeBuilder pickupLimitBuilder;
        @Property(comp = @Comp(gte = 1), priority = 1000, value = "groovyscript.wiki.pickuplimits.limit_builder.addStacks.value")
        private List<ItemStack> addStacks;
        @Property(comp = @Comp(gte = 0), priority = 1001)
        private List<OreDictIngredient> addOreDict;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1002, value = "groovyscript.wiki.pickuplimits.limit_builder.setStackLimitFunction.value")
        private ILimitFunction setStackLimitFunction;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1003, value = "groovyscript.wiki.pickuplimits.limit_builder.setAllowOverLimit.value")
        private boolean setAllowOverLimit;
        @Property(comp = @Comp(gte = 0), priority = 1004, value = "groovyscript.wiki.pickuplimits.limit_builder.addEncumberedEffect.value")
        private Map<Potion, Integer> addEncumberedEffect;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1005, value = "groovyscript.wiki.pickuplimits.limit_builder.setLimitMessage.value")
        private String setLimitMessage;
        @Property(comp = @Comp(gte = 0, eq = 1), priority = 1006, value = "groovyscript.wiki.pickuplimits.limit_builder.setLimitTooltip.value")
        private String setLimitTooltip;
        @Property(comp = @Comp(gte = 0), priority = 1007, value = "groovyscript.wiki.pickuplimits.limit_builder.addArmorLimitAdjustment.value")
        private Map<ItemStack, Integer> addArmorLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1008, value = "groovyscript.wiki.pickuplimits.limit_builder.addBaubleLimitAdjustment.value")
        private Map<ItemStack, Integer> addBaubleLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1009, value = "groovyscript.wiki.pickuplimits.limit_builder.addEnchantmentLimitAdjustment.value")
        private Map<Enchantment, Integer> addEnchantmentLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1010, value = "groovyscript.wiki.pickuplimits.limit_builder.addPotionLimitAdjustment.value")
        private Map<Potion, Integer> addPotionLimitAdjustment;
        @Property(comp = @Comp(gte = 0), priority = 1011, value = "groovyscript.wiki.pickuplimits.limit_builder.addStagedLimitOverride.value")
        private Map<String, Integer> addStagedLimitOverride;
        @Property(comp = @Comp(gte = 0), priority = 1012, value = "groovyscript.wiki.pickuplimits.limit_builder.addStagedStackRemovals.value")
        private Map<String, List<ItemStack>> addStagedStackRemovals;
        @Property(comp = @Comp(gte = 0), priority = 1013)
        private Map<String, List<OreDictIngredient>> addStagedOreRemovals;

        public RecipeBuilder(String groupName, int defaultLimit) {
            super(new PickupLimitBuilder(groupName, defaultLimit));
        }

        @GroovyBlacklist
        @Override
        public RecipeBuilder getThis() {
            return this;
        }

        @RecipeBuilderMethodDescription
        public RecipeBuilder addOreDict(OreDictIngredient oreDict) {
            this.getBuilder().addOreDictToGroup(oreDict.getOreDict());
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.gamestages)
        @RecipeBuilderMethodDescription
        public RecipeBuilder addStagedOreRemovals(String stageName, OreDictIngredient... oreDictIngredients) {
            for (OreDictIngredient oreDictIngredient : oreDictIngredients) {
                this.getBuilder().addStagedOreGroupRemoval(stageName, oreDictIngredient.getOreDict());
            }
            return this;
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            msg.add(LimitRegistry.getAllEquipmentLimitGroups().containsKey(this.getBuilder().getGroupName()), "Duplicate group name found for pickup limit group " + this.getBuilder().getGroupName());
            msg.add(this.getBuilder().getGroupStacks().isEmpty(), "Pickup limit group is empty. No items or valid oreDicts are registered");
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable PickupLimitGroup register() {
            GroovyLog.get().info(String.format("Registering Pickup Limit group: '%s'", this.getBuilder().getGroupName()));
            if (!validate()) return null;
            PickupLimitGroup group = this.getBuilder().build();
            LimitRegistry.addPickupLimitGroup(group);
            return group;
        }
    }
}
