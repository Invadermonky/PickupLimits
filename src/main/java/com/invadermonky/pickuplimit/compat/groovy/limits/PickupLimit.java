package com.invadermonky.pickuplimit.compat.groovy.limits;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.builders.PickupLimitBuilder;
import com.invadermonky.pickuplimit.limits.groups.PickupLimitGroup;
import com.invadermonky.pickuplimit.registry.LimitRegistry;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

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

        @GroovyBlacklist
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
