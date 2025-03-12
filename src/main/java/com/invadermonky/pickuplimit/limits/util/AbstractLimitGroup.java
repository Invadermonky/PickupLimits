package com.invadermonky.pickuplimit.limits.util;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.pickuplimit.util.libs.ModIds;
import gnu.trove.map.hash.THashMap;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractLimitGroup<T extends AbstractLimitBuilder<?,?>> {
    protected final String groupName;
    protected final int defaultLimit;
    protected final NonNullList<ItemStack> groupStacks;

    protected final String pickupMessage;
    protected final Map<ItemStack, Integer> armorLimitAdjustments;
    protected final Map<ItemStack, Integer> baubleLimitAdjustments;
    protected final Map<String, Integer> stageLimitOverride;
    protected final THashMap<String, NonNullList<ItemStack>> stagedStackRemovals;

    public AbstractLimitGroup(T builder) {
        this.groupName = builder.getGroupName();
        this.defaultLimit = builder.getDefaultLimit();
        this.groupStacks = builder.getGroupStacks();
        this.pickupMessage = builder.getPickupLimitMessage();
        this.armorLimitAdjustments = builder.getArmorLimits();
        this.baubleLimitAdjustments = builder.getBaubleLimits();
        this.stageLimitOverride = builder.getStageLimitOverride();
        this.stagedStackRemovals = builder.getStagedStackRemovals();
        this.cleanLists();
    }

    private void cleanLists() {
        this.groupStacks.removeIf(ItemStack::isEmpty);
        this.armorLimitAdjustments.keySet().removeIf(ItemStack::isEmpty);
        this.baubleLimitAdjustments.keySet().removeIf(ItemStack::isEmpty);
        this.stageLimitOverride.keySet().removeIf(stage -> stage == null || stage.trim().isEmpty());
        this.stagedStackRemovals.entrySet().removeIf(entry -> {
            if(entry.getKey() == null || entry.getKey().trim().isEmpty())
                return true;
            entry.getValue().removeIf(ItemStack::isEmpty);
            return entry.getValue().isEmpty();
        });
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getPickupMessage() {
        return this.pickupMessage;
    }

    public abstract int getStackLimitValue(EntityPlayer player, ItemStack stack);

    public int getLimit(EntityPlayer player) {
        int limit = this.getStageLimitOverride(player);
        if(limit < 0) {
            return -1;
        }

        for(ItemStack armorStack : player.getArmorInventoryList()) {
            //Handling Armor Limit Adjustments
            for(ItemStack limitArmor : this.armorLimitAdjustments.keySet()) {
                if(ItemStack.areItemsEqualIgnoreDurability(armorStack, limitArmor) && (!limitArmor.hasTagCompound() || ItemStack.areItemStackTagsEqual(armorStack, limitArmor))) {
                    limit += this.armorLimitAdjustments.get(limitArmor);
                }
            }
        }

        //Handling bauble limit adjustments
        if(ModIds.baubles.isLoaded()) {
            for (ItemStack bauble : this.baubleLimitAdjustments.keySet()) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if(ItemStack.areItemsEqual(bauble, stack) && (!bauble.hasTagCompound() || ItemStack.areItemStackTagsEqual(bauble, stack))) {
                        limit += this.baubleLimitAdjustments.get(bauble);
                    }
                }
            }
        }

        return Math.max(0, limit);
    }

    private int getStageLimitOverride(EntityPlayer player) {
        int limit = this.defaultLimit;
        if(ModIds.gamestages.isLoaded()) {
            for(String stage : this.stageLimitOverride.keySet()) {
                if(GameStageHelper.hasStage(player, stage)) {
                    limit = this.stageLimitOverride.get(stage);
                }
            }
        }
        return limit;
    }

    public NonNullList<ItemStack> getLimitStacks(EntityPlayer player) {
        if(ModIds.gamestages.isLoaded()) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            this.groupStacks.stream().map(ItemStack::copy).forEach(stacks::add);

            for(String stageName : this.stagedStackRemovals.keySet()) {
                if(GameStageHelper.hasStage(player, stageName)) {
                    for(ItemStack removeStack : this.stagedStackRemovals.get(stageName)) {
                        stacks.removeIf(stack -> ItemStack.areItemStacksEqual(stack, removeStack));
                    }
                }
            }
            return stacks;
        } else {
            return this.groupStacks;

        }
    }

    public boolean matches(EntityPlayer player, ItemStack stack) {
        if(!stack.isEmpty()) {
            for (ItemStack limitStack : this.getLimitStacks(player)) {
                if (OreDictionary.itemMatches(limitStack, stack, false)) {
                    if (!limitStack.hasTagCompound() || ItemStack.areItemStackTagsEqual(stack, limitStack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLimitGroup<?> that = (AbstractLimitGroup<?>) o;
        return Objects.equals(this.getGroupName(), that.getGroupName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getGroupName());
    }
}
