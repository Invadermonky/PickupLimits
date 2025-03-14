package com.invadermonky.pickuplimit.compat.groovy.limits;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.PickupLimitGroup;
import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = PickupLimits.MOD_ID)
public class PickupLimit extends VirtualizedRegistry<PickupLimitGroup> {
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
            example = {@Example("'stone', 256, item('minecraft:stone'), item('minecraft:cobblestone')")}
    )
    public void simplePickupLimit(String groupName, int defaultLimit, ItemStack... stacks) {
        this.simplePickupLimit(groupName, defaultLimit, null, stacks);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {@Example("'stone', 256, 'You cannot carry any more stone', item('minecraft:stone'), item('minecraft:cobblestone')")}
    )
    public void simplePickupLimit(String groupName, int defaultLimit, @Nullable String message, ItemStack... stacks) {
        this.recipeBuilder(groupName, defaultLimit)
                .addStacks(stacks)
                .setLimitMessage(message)
                .register();
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {@Example("'gems', 32, ore('gemDiamond'), ore('gemEmerald')")}
    )
    public void simplePickupLimit(String groupName, int defaultLimit, OreDictIngredient... oreDicts) {
        this.simplePickupLimit(groupName, defaultLimit, null, oreDicts);
    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            example = {
                    @Example("'gems', 32, 'Gems are falling out of your pockets', ore('gemDiamond'), ore('gemEmerald')")
            }
    )
    public void simplePickupLimit(String groupName, int defaultLimit, @Nullable String message, OreDictIngredient... oreDicts) {
        RecipeBuilder builder = new RecipeBuilder(groupName, defaultLimit);
        for(OreDictIngredient oreDict : oreDicts) {
            builder.addOreDict(oreDict);
        }
        builder.setLimitMessage(message);
        builder.register();
    }

    @RecipeBuilderDescription
    @MethodDescription(
            type = MethodDescription.Type.ADDITION,
            priority = 1001
    )
    public RecipeBuilder pickupLimitBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }



    public static class RecipeBuilder extends AbstractRecipeBuilder<PickupLimitGroup> {
        private final PickupLimitBuilder builder;

        @GroovyBlacklist
        public RecipeBuilder(String groupName, int defaultLimit) {
            this.builder = new PickupLimitBuilder(groupName, defaultLimit);
        }

        @RecipeBuilderDescription
        public RecipeBuilder addStacks(ItemStack... stacks) {
            this.builder.addStacksToGroup(stacks);
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder addOreDict(OreDictIngredient oreDict) {
            this.builder.addOreDictToGroup(oreDict.getOreDict());
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
        public RecipeBuilder addStagedStackRemoval(String stageName, ItemStack... stacks) {
            this.builder.addStagedStackGroupRemoval(stageName, stacks);
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.gamestages)
        @RecipeBuilderDescription
        public RecipeBuilder addStagedOreRemovals(String stageName, OreDictIngredient... oreDictIngredients) {
            for(OreDictIngredient oreDictIngredient : oreDictIngredients) {
                this.builder.addStagedOreGroupRemoval(stageName, oreDictIngredient.getOreDict());
            }
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Error creating new Pickup Limit group.";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {}

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable PickupLimitGroup register() {
            if(!validate()) return null;
            PickupLimitGroup group = this.builder.build();
            LimitRegistry.addPickupLimitGroup(group);
            return group;
        }
    }
}
