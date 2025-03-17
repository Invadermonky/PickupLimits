package com.invadermonky.pickuplimit.limits.groups;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.pickuplimit.limits.api.ILimitFunction;
import com.invadermonky.pickuplimit.limits.builders.AbstractLimitBuilder;
import com.invadermonky.pickuplimit.limits.caches.AbstractGroupCache;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.map.hash.THashMap;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractLimitGroup<T extends AbstractLimitBuilder<?, ?>> {
    protected final String groupName;
    protected final int defaultLimit;
    protected final NonNullList<ItemStack> groupStacks;
    protected final ILimitFunction stackLimitFunction;
    protected final String limitMessage;
    protected final String limitTooltip;
    protected final boolean allowOverLimit;
    protected final Map<ItemStack, Integer> armorLimitAdjustments;
    protected final Map<ItemStack, Integer> baubleLimitAdjustments;
    protected final Map<Tuple<Enchantment, Integer>, Integer> enchantmentLimitAdjustments;
    protected final Map<Tuple<Potion, Integer>, Integer> potionLimitAdjustments;
    protected final Map<String, Integer> stageLimitOverride;
    protected final THashMap<String, NonNullList<ItemStack>> stagedStackRemovals;
    protected final THashMap<Potion, Integer> encumberedEffects;

    public AbstractLimitGroup(T builder) {
        this.groupName = builder.getGroupName();
        this.defaultLimit = builder.getDefaultLimit();
        this.groupStacks = builder.getGroupStacks();
        this.stackLimitFunction = builder.getItemLimitFunction();
        this.limitMessage = builder.getLimitMessage();
        this.limitTooltip = builder.getLimitTooltip();
        this.allowOverLimit = builder.getAllowOverLimit();
        this.armorLimitAdjustments = builder.getArmorLimitAdjustments();
        this.baubleLimitAdjustments = builder.getBaubleLimitAdjustments();
        this.enchantmentLimitAdjustments = builder.getEnchantmentLimitAdjustments();
        this.potionLimitAdjustments = builder.getPotionLimitAdjustments();
        this.stageLimitOverride = builder.getStageLimitOverride();
        this.stagedStackRemovals = builder.getStagedStackRemovals();
        this.encumberedEffects = builder.getEncumberedEffects();
        this.cleanLists();
    }

    private void cleanLists() {
        this.groupStacks.removeIf(ItemStack::isEmpty);
        this.armorLimitAdjustments.keySet().removeIf(ItemStack::isEmpty);
        this.baubleLimitAdjustments.keySet().removeIf(ItemStack::isEmpty);
        this.enchantmentLimitAdjustments.keySet().removeIf(Objects::isNull);
        this.potionLimitAdjustments.keySet().removeIf(Objects::isNull);
        this.stageLimitOverride.keySet().removeIf(stage -> stage == null || stage.trim().isEmpty());
        this.stagedStackRemovals.entrySet().removeIf(entry -> {
            if (entry.getKey() == null || entry.getKey().trim().isEmpty())
                return true;
            entry.getValue().removeIf(ItemStack::isEmpty);
            return entry.getValue().isEmpty();
        });
        this.encumberedEffects.keySet().removeIf(Objects::isNull);
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getLimitMessage() {
        return this.limitMessage;
    }

    public boolean hasLimitTooltip() {
        return this.getLimitTooltip() != null;
    }

    public String getLimitTooltip() {
        return this.limitTooltip;
    }

    @Nullable
    public ILimitFunction getStackLimitFunction() {
        return this.stackLimitFunction;
    }

    public abstract int getStackLimitValue(EntityPlayer player, ItemStack stack);

    public int getLimit(EntityPlayer player) {
        int limit = this.getLimitWithStageOverride(player);

        if (limit < 0) {
            return -1;
        }

        for (ItemStack armorStack : player.getArmorInventoryList()) {
            for (ItemStack limitArmor : this.armorLimitAdjustments.keySet()) {
                //Handling Armor Limit Adjustments
                if (ItemStack.areItemsEqualIgnoreDurability(armorStack, limitArmor) && (!limitArmor.hasTagCompound() || ItemStack.areItemStackTagsEqual(armorStack, limitArmor))) {
                    limit += this.armorLimitAdjustments.get(limitArmor);
                }

                //Handling Enchantment Limit Adjustments
                if (!this.enchantmentLimitAdjustments.isEmpty() && armorStack.isItemEnchanted()) {
                    for (Tuple<Enchantment, Integer> limitPair : this.enchantmentLimitAdjustments.keySet()) {
                        int enchLevel = EnchantmentHelper.getEnchantmentLevel(limitPair.getFirst(), armorStack);
                        if (enchLevel > 0 && (limitPair.getSecond() == -1 || limitPair.getSecond() == enchLevel)) {
                            limit += this.enchantmentLimitAdjustments.get(limitPair);
                        }
                    }
                }
            }
        }

        //Handling bauble limit adjustments
        if (ModIds.baubles.isLoaded()) {
            for (ItemStack bauble : this.baubleLimitAdjustments.keySet()) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (ItemStack.areItemsEqual(bauble, stack) && (!bauble.hasTagCompound() || ItemStack.areItemStackTagsEqual(bauble, stack))) {
                        limit += this.baubleLimitAdjustments.get(bauble);
                    }
                }
            }
        }

        for (Tuple<Potion, Integer> limitPair : this.potionLimitAdjustments.keySet()) {
            PotionEffect effect = player.getActivePotionEffect(limitPair.getFirst());
            if (effect != null && (limitPair.getSecond() == -1 || limitPair.getSecond() == effect.getAmplifier())) {
                limit += this.potionLimitAdjustments.get(limitPair);
            }
        }

        return Math.max(0, limit);
    }

    protected int getLimitWithStageOverride(EntityPlayer player) {
        int limit = this.defaultLimit;
        if (ModIds.gamestages.isLoaded()) {
            for (String stage : this.stageLimitOverride.keySet()) {
                if (GameStageHelper.hasStage(player, stage)) {
                    limit = this.stageLimitOverride.get(stage);
                }
            }
        }
        return limit;
    }

    public NonNullList<ItemStack> getLimitStacks(EntityPlayer player) {
        if (ModIds.gamestages.isLoaded()) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            this.groupStacks.stream().map(ItemStack::copy).forEach(stacks::add);

            for (String stageName : this.stagedStackRemovals.keySet()) {
                if (GameStageHelper.hasStage(player, stageName)) {
                    for (ItemStack removeStack : this.stagedStackRemovals.get(stageName)) {
                        stacks.removeIf(stack -> ItemStack.areItemStacksEqual(stack, removeStack));
                    }
                }
            }
            return stacks;
        } else {
            return this.groupStacks;

        }
    }

    public abstract boolean matches(EntityPlayer player, ItemStack stack);

    /**
     * Returns true if the item was dropped into the world, false if it is still in the player's inventory.
     */
    public abstract boolean handleLimitDrop(EntityPlayer player, ItemStack stack, AbstractGroupCache<?> groupCache, boolean dropItem);

    public boolean shouldItemBeDropped() {
        return this.encumberedEffects.isEmpty() || this.allowOverLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractLimitGroup)) return false;
        AbstractLimitGroup<?> that = (AbstractLimitGroup<?>) o;
        return Objects.equals(getGroupName(), that.getGroupName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getGroupName());
    }
}
