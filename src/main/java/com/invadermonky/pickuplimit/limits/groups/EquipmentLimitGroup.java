package com.invadermonky.pickuplimit.limits.groups;

import com.invadermonky.pickuplimit.config.ConfigHandlerPL;
import com.invadermonky.pickuplimit.handlers.CommonEventHandler;
import com.invadermonky.pickuplimit.limits.builders.EquipmentLimitBuilder;
import com.invadermonky.pickuplimit.limits.caches.AbstractGroupCache;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.set.hash.THashSet;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class EquipmentLimitGroup extends AbstractLimitGroup<EquipmentLimitBuilder> {
    public static final EquipmentLimitGroup EMPTY = new EquipmentLimitBuilder(null, -1).build();

    protected final boolean matchAnyEnchant;
    protected final boolean ignoreItemEnchantCount;
    protected final boolean ignoreEnchantmentLevel;
    protected final boolean checkMainhand;
    protected final boolean checkOffhand;
    protected final Set<Enchantment> groupEnchants;
    protected final Map<String, Set<Enchantment>> stagedEnchantRemovals;

    public EquipmentLimitGroup(EquipmentLimitBuilder builder) {
        super(builder);
        this.matchAnyEnchant = builder.getMatchAnyEnchant();
        this.ignoreItemEnchantCount = builder.getIgnoreItemEnchantmentCount();
        this.ignoreEnchantmentLevel = builder.getIgnoreEnchantmentLevel();
        this.checkMainhand = builder.getCheckMainhand();
        this.checkOffhand = builder.getCheckOffhand();
        this.groupEnchants = builder.getGroupEnchants();
        this.stagedEnchantRemovals = builder.getStagedEnchantRemovals();
        this.cleanLists();
    }

    private void cleanLists() {
        if (this.matchAnyEnchant) {
            this.groupEnchants.clear();
            this.stagedEnchantRemovals.clear();
        } else {
            this.groupEnchants.removeIf(Objects::isNull);
            this.stagedEnchantRemovals.entrySet().removeIf(entry -> {
                if (entry.getKey() == null || entry.getKey().trim().isEmpty())
                    return true;
                entry.getValue().removeIf(Objects::isNull);
                return entry.getValue().isEmpty();
            });
        }
    }

    public boolean shouldCheckSlot(EntityEquipmentSlot slot) {
        return (slot != EntityEquipmentSlot.OFFHAND || this.checkOffhand) && (slot != EntityEquipmentSlot.MAINHAND || this.checkMainhand);
    }

    public Set<Enchantment> getLimitEnchants(EntityPlayer player) {
        if (ModIds.gamestages.isLoaded()) {
            Set<Enchantment> limitEnchants = new THashSet<>(this.groupEnchants);
            for (String stage : this.stagedEnchantRemovals.keySet()) {
                if (GameStageHelper.hasStage(player, stage)) {
                    limitEnchants.removeAll(this.stagedEnchantRemovals.get(stage));
                }
            }
            return limitEnchants;
        } else {
            return this.groupEnchants;
        }
    }

    @Override
    public int getStackLimitValue(EntityPlayer player, ItemStack stack) {
        int count = this.itemMatches(player, stack) ? stack.getCount() : 0;
        if (stack.isItemEnchanted()) {
            if (this.ignoreItemEnchantCount && this.enchantmentMatches(player, stack))
                return count + 1;

            count += EnchantmentHelper.getEnchantments(stack).entrySet().stream()
                    .filter(entry -> this.groupEnchants.contains(entry.getKey()) || this.matchAnyEnchant)
                    .mapToInt(entry -> this.ignoreEnchantmentLevel ? 1 : entry.getValue())
                    .sum();
        }
        return count;
    }

    @Override
    public boolean matches(EntityPlayer player, ItemStack stack) {
        return this.enchantmentMatches(player, stack) || this.itemMatches(player, stack);
    }

    @Override
    public boolean handleLimitDrop(EntityPlayer player, ItemStack stack, AbstractGroupCache<?> groupCache, boolean dropItem) {
        if (!this.encumberedEffects.isEmpty()) {
            final int duration = ConfigHandlerPL.pickup_limits.inventoryCheckInterval + 20;
            this.encumberedEffects.forEach((potion, amplifier) -> player.addPotionEffect(new PotionEffect(potion, duration, amplifier, true, false)));
        } else {
            player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0f, 1.0f);
            CommonEventHandler.sendLimitMessage(player, stack, groupCache, ConfigHandlerPL.pickup_limits.enableEquipmentLimitMessage);
            if (dropItem || !player.addItemStackToInventory(stack)) {
                player.dropItem(stack, true);
                return true;
            }
            groupCache.shrinkInvCount(stack);
        }
        return false;
    }

    private boolean enchantmentMatches(EntityPlayer player, ItemStack stack) {
        if (!stack.isEmpty() && stack.isItemEnchanted()) {
            if (this.matchAnyEnchant)
                return true;

            Set<Enchantment> limitEnchants = this.getLimitEnchants(player);
            for (Enchantment stackEnch : EnchantmentHelper.getEnchantments(stack).keySet()) {
                if (limitEnchants.contains(stackEnch))
                    return true;
            }
        }
        return false;
    }

    private boolean itemMatches(EntityPlayer player, ItemStack stack) {
        if (!stack.isEmpty() && this.getLimitWithStageOverride(player) != -1) {
            for (Ingredient limitIngredient : this.getLimitIngredients(player)) {
                if (limitIngredient.apply(stack)) {
                    return true;
                }
            }
        }
        return false;
    }
}
