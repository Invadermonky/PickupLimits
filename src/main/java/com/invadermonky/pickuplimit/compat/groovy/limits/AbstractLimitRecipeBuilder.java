package com.invadermonky.pickuplimit.compat.groovy.limits;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderMethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RecipeBuilderRegistrationMethod;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.invadermonky.pickuplimit.PickupLimits;
import com.invadermonky.pickuplimit.limits.api.ILimitFunction;
import com.invadermonky.pickuplimit.limits.builders.AbstractLimitBuilder;
import com.invadermonky.pickuplimit.limits.groups.AbstractLimitGroup;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = PickupLimits.MOD_ID)
public abstract class AbstractLimitRecipeBuilder<
        T extends AbstractLimitRecipeBuilder<T,S,U>,
        S extends AbstractLimitBuilder<?,?>,
        U extends AbstractLimitGroup<S>> extends AbstractRecipeBuilder<U> {

    private final S builder;

    @GroovyBlacklist
    public AbstractLimitRecipeBuilder(S builder) {
        this.builder = builder;
    }

    @GroovyBlacklist
    public abstract T getThis();

    @GroovyBlacklist
    public S getBuilder() {
        return this.builder;
    }

    @RecipeBuilderMethodDescription
    public T addStacks(ItemStack... stacks) {
        this.getBuilder().addStacksToGroup(stacks);
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T setStackLimitFunction(ILimitFunction function) {
        this.getBuilder().setItemLimitValueFunction(function);
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T setAllowOverLimit() {
        this.getBuilder().setAllowOverlimit();
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T addEncumberedEffect(Potion potion, int amplifier) {
        this.getBuilder().addEncumberedEffect(potion, amplifier);
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T setLimitMessage(@Nullable String pickupLimitMessage) {
        this.getBuilder().setLimitMessage(pickupLimitMessage);
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T setLimitTooltip(String tooltip) {
        this.getBuilder().setLimitTooltip(tooltip);
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T addArmorLimitAdjustment(ItemStack stack, int adjustment) {
        this.getBuilder().addArmorLimitAdjustment(stack, adjustment);
        return this.getThis();
    }

    @Optional.Method(modid = ModIds.ConstIds.baubles)
    @RecipeBuilderMethodDescription
    public T addBaubleLimitAdjustment(ItemStack stack, int adjustment) {
        this.getBuilder().addBaubleLimitAdjustment(stack, adjustment);
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T addEnchantmentLimitAdjustment(Enchantment enchantment, int enchantmentLevel, int adjustment) {
        this.getBuilder().addEnchantmentLimitAdjustment(enchantment, enchantmentLevel, adjustment);
        return this.getThis();
    }

    @RecipeBuilderMethodDescription
    public T addPotionLimitAdjustment(Potion potion, int amplifier, int adjustment) {
        this.getBuilder().addPotionLimitAdjustment(potion, amplifier, adjustment);
        return this.getThis();
    }

    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    @RecipeBuilderMethodDescription
    public T addStageLimitOverride(String stageName, int limitOverride) {
        this.getBuilder().addStageLimitOverride(stageName, limitOverride);
        return this.getThis();
    }

    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    @RecipeBuilderMethodDescription
    public T addStagedStackRemovals(String stageName, ItemStack... stacks) {
        this.getBuilder().addStagedStackGroupRemoval(stageName, stacks);
        return this.getThis();
    }

    @Override
    public String getErrorMsg() {
        return "Error creating new Limit Group.";
    }

    public @Nullable U build() {
        return this.register();
    }
}
