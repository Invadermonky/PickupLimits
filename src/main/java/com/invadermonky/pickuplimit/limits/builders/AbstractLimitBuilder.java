package com.invadermonky.pickuplimit.limits.builders;

import baubles.api.IBauble;
import com.invadermonky.pickuplimit.limits.api.ILimitFunction;
import com.invadermonky.pickuplimit.limits.groups.AbstractLimitGroup;
import com.invadermonky.pickuplimit.util.LogHelper;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.map.hash.THashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractLimitBuilder<T extends AbstractLimitBuilder<T, S>, S extends AbstractLimitGroup<?>> {
    protected final String groupName;
    protected final int defaultLimit;
    protected final NonNullList<Ingredient> groupIngredients = NonNullList.create();
    protected final Map<ItemStack, Integer> armorLimitAdjustments = new Object2IntOpenHashMap<>();
    protected final Map<ItemStack, Integer> baubleLimitAdjustments = new Object2IntOpenHashMap<>();
    protected final Map<Tuple<Enchantment, Integer>, Integer> enchantmentLimitAdjustments = new Object2IntOpenHashMap<>();
    protected final Map<Tuple<Potion, Integer>, Integer> potionLimitAdjustments = new Object2IntOpenHashMap<>();
    protected final LinkedHashMap<String, Integer> stageLimitOverride = new LinkedHashMap<>();
    protected final Map<String, NonNullList<Ingredient>> stagedStackRemovals = new THashMap<>();
    protected final Map<Potion, Integer> encumberedEffects = new Object2IntOpenHashMap<>();
    protected ILimitFunction itemLimitFunction;
    protected String limitMessage;
    protected String limitTooltip;
    protected boolean allowOverLimit;

    public AbstractLimitBuilder(String groupName, int defaultLimit) {
        this.groupName = groupName;
        this.defaultLimit = defaultLimit;
        this.itemLimitFunction = null;
    }

    protected abstract T getThis();

    /**
     * Returns the builder pickup limit group name
     */
    public String getGroupName() {
        return this.groupName;
    }

    /**
     * Returns the default group pickup limit
     */
    public int getDefaultLimit() {
        return this.defaultLimit;
    }

    /**
     * Adds all passed Ingredients to the group ItemStack list.
     *
     * @param ingredients Ingredients to add
     * @return this
     */
    public T addItemsToGroup(Ingredient... ingredients) {
        this.groupIngredients.addAll(Arrays.asList(ingredients));
        return this.getThis();
    }

    /**
     * Returns all ItemStacks that have a limited pickup defined by this pickup limit
     */
    public NonNullList<Ingredient> getGroupItems() {
        return this.groupIngredients;
    }

    public T setItemLimitValueFunction(ILimitFunction function) {
        this.itemLimitFunction = function;
        return getThis();
    }

    @Nullable
    public ILimitFunction getItemLimitFunction() {
        return this.itemLimitFunction;
    }

    /**
     * Returns the defined pickup limit message string that will be displayed in the pickup fails. This can be a localization
     * key or plain text.
     *
     * @return The pickup limit message string that will be displayed if the pickup fails.
     */
    public String getLimitMessage() {
        return this.limitMessage;
    }

    /**
     * Sets a new failed pickup message for this group. This can be plain text or a language key. Including a %s will
     * cause the value to include the display name of the ItemStack.
     *
     * @param limitMessage The new pickup message text or language key
     * @return this
     */
    public T setLimitMessage(String limitMessage) {
        if (limitMessage != null) {
            this.limitMessage = limitMessage;
        }
        return this.getThis();
    }

    public String getLimitTooltip() {
        return this.limitTooltip;
    }

    public T setLimitTooltip(String limitTooltip) {
        if (limitTooltip != null) {
            this.limitTooltip = limitTooltip;
        }
        return this.getThis();
    }

    public T setAllowOverlimit() {
        this.allowOverLimit = true;
        return this.getThis();
    }

    public boolean getAllowOverLimit() {
        return this.allowOverLimit;
    }

    public T addEncumberedEffect(Potion potion, int amplifier) {
        encumberedEffects.put(potion, amplifier);
        return this.getThis();
    }

    public Map<Potion, Integer> getEncumberedEffects() {
        return this.encumberedEffects;
    }

    /**
     * Adds a limit adjustment when the player has the passed armor equipped. This adjustment value can be positive or
     * negative.
     *
     * @param stack      The armor ItemStack
     * @param adjustment The limit adjustment when the armor is equipped
     * @return this
     */
    public T addArmorLimitAdjustment(ItemStack stack, int adjustment) {
        if (!stack.isEmpty()) {
            this.armorLimitAdjustments.put(stack, adjustment);
        } else {
            LogHelper.error("Error adding armor limit for " + stack.getItem().getRegistryName());
        }
        return this.getThis();
    }

    /**
     * Returns any defined equipped armor pickup limit adjustments.
     */
    public Map<ItemStack, Integer> getArmorLimitAdjustments() {
        return this.armorLimitAdjustments;
    }

    /**
     * Adds a limit adjustment when the player has the passed bauble equipped. This adjustment value can be positive
     * or negative.
     *
     * @param stack      The bauble ItemStack
     * @param adjustment The limit adjustment when the bauble is equipped
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.baubles)
    public T addBaubleLimitAdjustment(ItemStack stack, int adjustment) {
        if (!stack.isEmpty() && stack.getItem() instanceof IBauble) {
            baubleLimitAdjustments.put(stack, adjustment);
        } else {
            LogHelper.error("Error adding bauble limit for " + stack.getItem().getRegistryName());
        }
        return this.getThis();
    }

    /**
     * Returns any defined bauble pickup limit adjustments.
     */
    public Map<ItemStack, Integer> getBaubleLimitAdjustments() {
        return this.baubleLimitAdjustments;
    }

    public T addEnchantmentLimitAdjustment(Enchantment enchantment, int enchantmentLevel, int adjustment) {
        this.enchantmentLimitAdjustments.put(new Tuple<>(enchantment, enchantmentLevel), adjustment);
        return this.getThis();
    }

    public Map<Tuple<Enchantment, Integer>, Integer> getEnchantmentLimitAdjustments() {
        return this.enchantmentLimitAdjustments;
    }

    public T addPotionLimitAdjustment(Potion potion, int amplifier, int adjustment) {
        this.potionLimitAdjustments.put(new Tuple<>(potion, amplifier), adjustment);
        return this.getThis();
    }

    public Map<Tuple<Potion, Integer>, Integer> getPotionLimitAdjustments() {
        return this.potionLimitAdjustments;
    }

    /**
     * Creates a game stage pickup limit override. This value will override the default pickup limit. Stages <b>must</b>
     * be added in order. The last stage added will override any values before it provided the player has the required
     * stage.
     *
     * @param stageName     The GameStage stage name
     * @param limitOverride The pickup limit override for this stage
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public T addStagedLimitOverride(String stageName, int limitOverride) {
        this.stageLimitOverride.put(stageName, limitOverride);
        return this.getThis();
    }

    /**
     * Returns any defined GameStage group pickup limit overrides.
     */
    public LinkedHashMap<String, Integer> getStageLimitOverride() {
        return this.stageLimitOverride;
    }

    /**
     * Specifies any ItemStacks that will be removed from this group once the player obtains the specified GameStage.
     *
     * @param stageName   The name of the GameStage
     * @param ingredients Any ingredients that will be removed from this group limit when the player obtains the specified GameStage
     * @return this
     */
    @Optional.Method(modid = ModIds.ConstIds.gamestages)
    public T addStagedIngredientRemoval(String stageName, Ingredient... ingredients) {
        NonNullList<Ingredient> ingredientList = NonNullList.from(Ingredient.EMPTY, ingredients);
        ingredientList.removeIf(ingredient -> ingredient == Ingredient.EMPTY);
        if (this.stagedStackRemovals.containsKey(stageName)) {
            this.stagedStackRemovals.get(stageName).addAll(ingredientList);
        } else {
            this.stagedStackRemovals.put(stageName, ingredientList);
        }
        return this.getThis();
    }

    /**
     * Returns any defined GameStage group ItemStack removals.
     */
    public Map<String, NonNullList<Ingredient>> getStagedStackRemovals() {
        return this.stagedStackRemovals;
    }

    public abstract S build();
}
