package com.invadermonky.pickuplimit.limits;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.pickuplimit.util.ModIds;
import gnu.trove.map.hash.THashMap;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;
import java.util.Objects;

public class PickupLimitGroup {
    public static final PickupLimitGroup EMPTY = new PickupLimitGroup(new PickupLimitBuilder(null, -1));

    private final String groupName;
    private final int pickupLimit;
    private final NonNullList<ItemStack> pickupLimitStacks;

    private final String pickupMessage;
    private final Map<ItemStack, Integer> armorLimitAdjustments;
    private final Map<ItemStack, Integer> baubleLimitAdjustments;
    private final Map<String, Integer> stageLimitOverride;
    private final THashMap<String, NonNullList<ItemStack>> stagedGroupStackRemovals;

    public PickupLimitGroup(PickupLimitBuilder builder) {
        this.groupName = builder.getGroupName();
        this.pickupLimit = builder.getPickupLimit();
        this.pickupLimitStacks = builder.getGroupStacks();
        this.pickupMessage = builder.getPickupLimitMessage();
        this.armorLimitAdjustments = builder.getArmorLimits();
        this.baubleLimitAdjustments = builder.getBaubleLimits();
        this.stageLimitOverride = builder.getStageLimitOverride();
        this.stagedGroupStackRemovals = builder.getStagedGroupStackRemovals();
        this.cleanLists();
    }

    private void cleanLists() {
        this.pickupLimitStacks.removeIf(ItemStack::isEmpty);
        this.armorLimitAdjustments.keySet().removeIf(ItemStack::isEmpty);
        this.baubleLimitAdjustments.keySet().removeIf(ItemStack::isEmpty);
        this.stageLimitOverride.keySet().removeIf(stage -> stage == null || stage.trim().isEmpty());
        this.stagedGroupStackRemovals.entrySet().removeIf(entry -> {
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

    public int getPickupLimit(EntityPlayer player) {
        int limit = this.getLimit(player);
        if(limit < 0) {
            return -1;
        }

        for(ItemStack armorStack : player.getArmorInventoryList()) {
            //Handling Armor Limit Adjustments
            for(ItemStack armorAdjustment : this.armorLimitAdjustments.keySet()) {
                if(ItemStack.areItemsEqualIgnoreDurability(armorStack, armorAdjustment)) {
                    limit += this.armorLimitAdjustments.get(armorAdjustment);
                }
            }
        }

        //Handling bauble limit adjustments
        if(ModIds.baubles.isLoaded()) {
            for (ItemStack bauble : this.baubleLimitAdjustments.keySet()) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                for(int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if(ItemStack.areItemsEqual(bauble, stack)) {
                        limit += this.baubleLimitAdjustments.get(bauble);
                    }
                }
            }
        }

        return Math.max(0, limit);
    }

    private int getLimit(EntityPlayer player) {
        int limit = this.pickupLimit;
        if(ModIds.gamestages.isLoaded()) {
            for(String stage : this.stageLimitOverride.keySet()) {
                if(GameStageHelper.hasStage(player, stage)) {
                    limit = this.stageLimitOverride.get(stage);
                }
            }
        }

        return limit;
    }

    public NonNullList<ItemStack> getPickupLimitStacks(EntityPlayer player) {
        if(ModIds.gamestages.isLoaded()) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            this.pickupLimitStacks.stream().map(ItemStack::copy).forEach(stacks::add);

            for(String stageName : this.stagedGroupStackRemovals.keySet()) {
                if(GameStageHelper.hasStage(player, stageName)) {
                    for(ItemStack removeStack : this.stagedGroupStackRemovals.get(stageName)) {
                        stacks.removeIf(stack -> ItemStack.areItemStacksEqual(stack, removeStack));
                    }
                }
            }
            return stacks;
        } else {
            return this.pickupLimitStacks;

        }
    }

    public boolean matches(EntityPlayer player, ItemStack stack) {
        if(!stack.isEmpty()) {
            for (ItemStack limitStack : this.getPickupLimitStacks(player)) {
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
        PickupLimitGroup that = (PickupLimitGroup) o;
        return Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(groupName);
    }
}
