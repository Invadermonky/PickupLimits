package com.invadermonky.pickuplimit.compat.groovy;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderRegistrationMethod;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.pickuplimit.PickupLimit;
import com.invadermonky.pickuplimit.limits.PickupLimitBuilder;
import com.invadermonky.pickuplimit.limits.PickupLimitGroup;
import com.invadermonky.pickuplimit.limits.PickupLimitRegistry;
import com.invadermonky.pickuplimit.util.ModIds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = PickupLimit.MOD_ID)
public class PickupLimitGS extends VirtualizedRegistry<PickupLimitGroup> {
    @Override
    @GroovyBlacklist
    public void onReload() {
        PickupLimitRegistry.removeAllGroups();
    }

    @GroovyBlacklist
    public RecipeBuilder recipeBuilder(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }

    public void newSimpleLimit(String groupName, int defaultLimit, ItemStack... stacks) {
        this.recipeBuilder(groupName, defaultLimit)
                .addStacksToGroup(stacks)
                .register();
    }

    public void newSimpleLimit(String groupName, int defaultLimit, OreDictIngredient... oreDicts) {
        RecipeBuilder builder = new RecipeBuilder(groupName, defaultLimit);
        for(OreDictIngredient oreDict : oreDicts) {
            builder.addOreDictToGroup(oreDict);
        }
        builder.register();
    }

    @RecipeBuilderDescription
    public RecipeBuilder newLimit(String groupName, int defaultLimit) {
        return new RecipeBuilder(groupName, defaultLimit);
    }

    public static class RecipeBuilder extends AbstractRecipeBuilder<PickupLimitGroup> {
        private final PickupLimitBuilder builder;

        public RecipeBuilder(String groupName, int defaultLimit) {
            this.builder = new PickupLimitBuilder(groupName, defaultLimit);
        }

        @RecipeBuilderDescription
        public RecipeBuilder addStacksToGroup(ItemStack... stacks) {
            this.builder.addStacksToGroup(stacks);
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder addOreDictToGroup(OreDictIngredient oreDict) {
            this.builder.addOreDictToGroup(oreDict.getOreDict());
            return this;
        }

        @RecipeBuilderDescription
        public RecipeBuilder setPickupLimitMessage(String pickupLimitMessage) {
            this.builder.setPickupLimitMessage(pickupLimitMessage);
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
        public RecipeBuilder addStagedStackGroupRemoval(String stageName, ItemStack... stacks) {
            this.builder.addStagedStackGroupRemoval(stageName, stacks);
            return this;
        }

        @Optional.Method(modid = ModIds.ConstIds.gamestages)
        @RecipeBuilderDescription
        public RecipeBuilder addStagedOreGroupRemovals(String stageName, OreDictIngredient... oreDictIngredients) {
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
        public void validate(GroovyLog.Msg msg) {

        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable PickupLimitGroup register() {
            if(!validate()) return null;
            PickupLimitGroup group = this.builder.build();
            PickupLimitRegistry.addPickupLimitGroup(group);
            return group;
        }
    }
}
